//
// Created by konek on 7/14/2019.
//

#ifndef GLES3JNI_FUNCTIONPOINTER_H
#define GLES3JNI_FUNCTIONPOINTER_H
#define functionPointerDeclare0(type, name) type (*name)()
#define functionPointerDeclare1(type, name, type1) type (*name)(type1)
#define functionPointerDeclare2(type, name, type1, type2) type (*name)(type1, type2)
#define functionPointerDeclare3(type, name, type1, type2) type (*name)(type1, type2, type3)
#define functionPointerDeclare4(type, name, type1, type2, type3, type4) type (*name)(type1, type2, type3, type4)
#define functionPointerCast0(type) type (*)()
#define functionPointerCast1(type, type1) type (*)(type1)
#define functionPointerCast2(type, type1, type2) type (*)(type1, type2)
#define functionPointerCast3(type, type1, type2, type3) type (*)(type1, type2, type3)
#define functionPointerCast4(type, type1, type2, type3, type4) type (*)(type1, type2, type3, type4)
#define functionPointerAssign0(type, f, what) f = (functionPointerCast0(type)) what
#define functionPointerAssign1(type, f, what, type1) f = (functionPointerCast1(type, type1)) what
#define functionPointerAssign2(type, f, what, type1, type2) f = (functionPointerCast2(type, type1, type2)) what
#define functionPointerAssign3(type, f, what, type1, type2, type3) f = (functionPointerCast2(type, type1, type2, type3)) what
#define functionPointerAssign4(type, f, what, type1, type2, type3, type4) f = (functionPointerCast4(type, type1, type2, type3, type4)) what
#define functionPointerDeclareAndAssign0(type, name, what) functionPointerDeclare0(type, name); functionPointerAssign0(type, name, what)
#define functionPointerDeclareAndAssign1(type, name, what, type1) functionPointerAssign1(type, name, type1); functionPointerDeclare1(type, name, what, type1)
#define functionPointerDeclareAndAssign2(type, name, what, type1, type2) functionPointerAssign2(type, name, type1, type2); functionPointerDeclare2(type, name, what, type1, type2)
#define functionPointerDeclareAndAssign3(type, name, what, type1, type2, type3) functionPointerAssign3(type, name, type1, type2, type3); functionPointerDeclare3(type, name, what, type1, type2, type3)
#define functionPointerDeclareAndAssign4(type, name, what, type1, type2, type3, type4) functionPointerAssign4(type, name, type1, type2, type3, type4); functionPointerDeclare4(type, name, what, type1, type2, type3, type4)
#endif //GLES3JNI_FUNCTIONPOINTER_H
