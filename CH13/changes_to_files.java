//Parser.java before:
private Stmt classDeclaration() {
    Token name = consume(IDENTIFIER, "Expect class name.");

    Expr.Variable superclass = null;
    if (match(LESS)) {
      consume(IDENTIFIER, "Expect superclass name.");
      superclass = new Expr.Variable(previous());
    }

    consume(LEFT_BRACE, "Expect '{' before class body.");

    List<Stmt.Function> methods = new ArrayList<>();
    while (!check(RIGHT_BRACE) && !isAtEnd()) {
      methods.add(function("method"));
    }

    consume(RIGHT_BRACE, "Expect '}' after class body.");
    return new Stmt.Class(name, superclass, methods);

  }

//Parser.java after: 

private Stmt classDeclaration() {
    Token name = consume(IDENTIFIER, "Expect class name.");

    List<Expr.variable> superclasses = new Arraylist<>();  //challenge 1 chapter 13
    if (match(LESS)) {
      do {
        consume(IDENTIFIER, "Expect superclass name.");
        superclasses.add(new Expr.Varibale(previous()));
      } while (match(COMMA));
    }
  
    Expr.Variable superclass = null;
    if (match(LESS)) {
      consume(IDENTIFIER, "Expect superclass name.");
      superclass = new Expr.Variable(previous());
    }

    consume(LEFT_BRACE, "Expect '{' before class body.");

    List<Stmt.Function> methods = new ArrayList<>();
    while (!check(RIGHT_BRACE) && !isAtEnd()) {
      methods.add(function("method"));
    }

    consume(RIGHT_BRACE, "Expect '}' after class body.");
    return new Stmt.Class(name, superclass, methods);
  }




//Resolver.java before (Challenge 1 Chapter 13):
 @Override
  public Void visitClassStmt(Stmt.Class stmt) {
    ClassType enclosingClass = currentClass;
    currentClass = ClassType.CLASS;
   
    declare(stmt.name);
    define(stmt.name);

    if (stmt.superclass != null &&
        stmt.name.lexeme.equals(stmt.superclass.name.lexeme)) {
      Lox.error(stmt.superclass.name,
          "A class can't inherit from itself.");
    }

    if (stmt.superclass != null) {
      currentClass = ClassType.SUBCLASS;
      resolve(stmt.superclass);
    }

    if (stmt.superclass != null) {
      beginScope();
      scopes.peek().put("super", true);
    }

    beginScope();
    scopes.peek().put("this", true);

   for (Stmt.Function method : stmt.methods) {
      FunctionType declaration = FunctionType.METHOD;
      if (method.name.lexeme.equals("init")) {
        declaration = FunctionType.INITIALIZER;
      }
      resolveFunction(method, declaration); 
    }

    endScope();

    if (stmt.superclass != null) endScope();

    currentClass = enclosingClass;
    return null;
  }

//Resolver.java after (Challenge 1 Chapter 13):
@Override
  public Void visitClassStmt(Stmt.Class stmt) {
    ClassType enclosingClass = currentClass;
    currentClass = ClassType.CLASS;
   
    declare(stmt.name);
    define(stmt.name);

   for (Expr.Variable superclass : stmt.superclasses) {
    if (stmt.name.lexeme.equals(stmt.superclass.name.lexeme)) {
      Lox.error(superclass.name,
          "A class can't inherit from itself.");
    }
   }

    if (!stmt.superclasses.isEmpty() {
      currentClass = ClassType.SUBCLASS;
      for (Expr.Variable superclass : stmt.superclasses) {
        resolve(superclass);
      }
    }

    if (!stmt.superclasses.isEmpty()) {
      beginScope();
      scopes.peek().put("super", true);
    }

    beginScope();
    scopes.peek().put("this", true);

   for (Stmt.Function method : stmt.methods) {
      FunctionType declaration = FunctionType.METHOD;
      if (method.name.lexeme.equals("init")) {
        declaration = FunctionType.INITIALIZER;
      }
      resolveFunction(method, declaration); 
    }

    endScope();

    if (stmt.superclass != null) endScope();

    currentClass = enclosingClass;
    return null;
  }


//Stmt.java before (Challenge 1 Chapter 13):
static class Class extends Stmt {
    Class(Token name, Expr.Variable superclass, List<Stmt.Function> methods) {
      this.name = name;
      this.superclass = superclass;
      this.methods = methods;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitClassStmt(this);
    }

    final Token name;
    final Expr.Variable superclass;
    final List<Stmt.Function> methods;
  }


//Stmt.java after (Challenge 1 Chapter 13):
static class Break extends Stmt {
  Break(){}

  @Override
  <R> R accept(Visitor<R> visitor){
    reutrn visitor.visitBreakStmt(this);
  }
}
static class Class extends Stmt {
    Class(Token name, List<Expr.Variable> superclasses, List<Stmt.Function> methods) {
      this.name = name;
      this.superclasses = superclasses;
      this.methods = methods;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitClassStmt(this);
    }

    final Token name;
    final Expr.Variable superclasses;
    final List<Stmt.Function> methods;
  }


//ASTPrinter before (Challenge 1 Ch 13):
@Override
  public String visitClassStmt(Stmt.Class stmt) {
    StringBuilder builder = new StringBuilder();
    builder.append("(class " + stmt.name.lexeme);

    if (stmt.superclass != null) {
      builder.append(" < " + print(stmt.superclass));
    }

    for (Stmt.Function method : stmt.methods) {
      builder.append(" " + print(method));
    }

    builder.append(")");
    return builder.toString();
  }

//ASTPrinter after (Challenge 1 Ch 13):
@Override
  public String visitBreakStmt(Stmt.Break stmt){
  return "break;";
}

@Override
  public String visitClassStmt(Stmt.Class stmt) {
    StringBuilder builder = new StringBuilder();
    builder.append("(class " + stmt.name.lexeme);

    if (!stmt.superclasses.isEmpty()) {
      builder.append(" < ");
      for (Expr.Variable superclass : stmt.superclasses){
        builder.append(" " + print(superclass));
      }
    }

    for (Stmt.Function method : stmt.methods) {
      builder.append(" " + print(method));
    }

    builder.append(")");
    return builder.toString();
  }


//TokenType.java addition (Challenge 2 ch 13):
INNER

//Scanner.jave addition (Challnege 2 ch 13):
keywords.put("inner", INNER);

//GenerateAst.jave addition (Challnege 2 ch 13):
"Inner    : Token keyword",

if (match(Inner)) return new Expr.Inner(previous());
