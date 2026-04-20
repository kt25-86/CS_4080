//LoxClass findMethod changes before:
LoxFunction findMethod(String name) {
    if (methods.containsKey(name)) {
      return methods.get(name);
    }

    if (superclass != null) {
      return superclass.findMethod(name);
    }

    return null;
  }

//LoxClass findMethod changes after:
LoxFunction findMethod(LoxInstance instance, String name) {
  LoxFunction method = null;
  LoxFunction inner = null;
  LoxClass klass = this;
  while (klass != null) {
    if (klass.methods.containsKey(name)) {
      inner = method;
      method = klass.methods.get(name);
    }

    klass = klass.superclass;
  }

  if (method != null) {
    return method.bind(instance, inner);
  }

  return null;
}

//LoxClass call before:
 @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod("init");
    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }
    return instance;
  }

//LoxClass call after:
public Object call(Interpreter interpreter, List<Object> arguments) {
  LoxInstance instance = new LoxInstance(this);
  LoxFunction initializer = findMethod(instance, "init");  //edited
  if (initializer != null) {
    initializer.call(interpreter, arguments);  //edited
  }

  return instance;
}

//LoxFunction bind before:
LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
    return new LoxFunction(declaration, environment, isInitializer);
  }

//LoxFunction bind after:
LoxFunction bind(LoxInstance instance, LoxFunction inner) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
    environment.define("inner", inner);
    return new LoxFunction(declaration, environment, isInitializer);
}

//Resolver.java visitClassStmt addition:
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
    scopes.peek().put("inner", true); //addition for challenge 2

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


