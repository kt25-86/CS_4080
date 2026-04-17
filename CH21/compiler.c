static uint8_t identifierConstant(Token* name){
  for(int i = 0; i<currentChunk()->constants.count; ++i){
    Value existing = currentChunk->constants.values[i];
    if(!IS_STRING(existing)) continue;
    ObjString* existingStr = AS_STRING(existing);

    if(existingStr->length == name->length && memcmp(existingStr->chars, name->start, name->length) == 0){
      return(uint8_t)i;
  }
    return makeConstant(OBJ_VAL(copyString(name->start, name->length)));
}
