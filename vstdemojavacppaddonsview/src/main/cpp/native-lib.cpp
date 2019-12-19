#include <jni.h>
#include <string>
#include <android/log.h>
extern "C" JNIEXPORT jstring JNICALL
Java_vst_demo_java_cpp_addons_view_main_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    __android_log_print(ANDROID_LOG_ERROR, "CPP", "holy crap i work!");
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}