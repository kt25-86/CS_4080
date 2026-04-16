#ifndef clox_vm_h
#define clox_vm_h

#include "chunk.h"
#include "value.h"
#define STACK_MAX 256

typedef struct {
  Chunk* chunk;
  uint8_t* ip;
  Value* stack;
  Value* stackTop;
  int stackCapacity;
} VM;

typedef enum {
  INTERPRET_OK,
  INTERPRET_COMPILE_ERROR,
  INTERPRET_RUNTIME_ERROR
} InterpretResult;

void initVM(){
  vm.stackCapacity = 256;
  vm.stack = malloc(sizeof(Value) * vm.stackCapacity);
  resetStack();
}

void freeVM(){
  free(vm.stack);
}

InterpretResult interpret(Chunk* chunk);
void push(Value value){
  if(vm.stackTop - vm.stack >= vm.stackCapacity){
    vm.stackCapacity *= 2;
    vm.stack = realloc(vm.stack, sizeof(Value) * vm.stackCapacity);
    vm.stackTop = vm.stack + vm.stackCapacity / 2;
  }
  *vm.stackTop = value;
  vm.stackTop++;
}
Value pop();

#endif
