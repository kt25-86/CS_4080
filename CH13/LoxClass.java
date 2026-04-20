package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;

class LoxClass implements LoxCallable {
  final String name;
  final List<LoxClass> superclasses;  //challenge 1 chapter 13
  private final Map<String, LoxFunction> methods;

  LoxClass(String name, List<LoxClass> superclasses, Map<String, LoxFunction> methods) { //challenge 1 chapter 13
    this.superclass = superclasses; //challenge 1 chapter 13
    this.name = name;
    this.methods = methods;
  }

  LoxFunction findMethod(String name) {
    if (methods.containsKey(name)) {
      return methods.get(name);
    }

    for (LoxClass superclass : superclasses) { //challenge 1 chapter 13
      LoxFunction method = superclass.findMethod(name);
      if (method != null) return method;
    }

    return null;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    LoxFunction initializer = findMethod("init");
    if (initializer != null) {
      initializer.bind(instance).call(interpreter, arguments);
    }
    
    return instance;
  }

  @Override
  public int arity() {
    LoxFunction initializer = findMethod("init");
    if (initializer == null) return 0;
    return initializer.arity();
  }
  
}
