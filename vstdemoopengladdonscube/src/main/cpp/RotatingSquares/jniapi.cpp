//
// Copyright 2011 Tero Saarni
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

#include <cstdint>
#include <jni.h>
#include <android/native_window.h> // requires ndk r5 or newer
#include <android/native_window_jni.h> // requires ndk r5 or newer

#include "logger.h"
#include "renderer.h"

#define LOG_TAG "EglSample"

// LIBRARIES ARE GLOBALLY LOADED.

// THE JVM NEVER LOADS MORE THAN ONE INSTANCE OF A LIBRARY INTO THE SAME ANDROID PROCESS
// AS A RESULT, NEW INSTANCES OF A LIBRARY CANNOT BE CREATED EASILY

// AND THUS LIBRARIES ARE RECOMMENDED TO BE MADE RE-ENTRANT


ANativeWindow *window = nullptr;
Renderer *renderer = nullptr;

extern "C" JNIEXPORT void JNICALL Java_vst_demo_opengl_addons_cube_NativeView_nativeOnStart(JNIEnv* jenv,
                                                                                       jclass type)
{
    LOG_INFO("nativeOnStart");
    LOG_INFO("renderer = %p", renderer);
    renderer = new Renderer();
    LOG_INFO("renderer = %p", renderer);
}

extern "C" JNIEXPORT void JNICALL Java_vst_demo_opengl_addons_cube_NativeView_nativeOnResume(JNIEnv* jenv,
                                                                                        jclass type)
{
    LOG_INFO("nativeOnResume");
    LOG_INFO("renderer = %p", renderer);
    renderer->start();
}

extern "C" JNIEXPORT void JNICALL Java_vst_demo_opengl_addons_cube_NativeView_nativeOnPause(JNIEnv* jenv,
                                                                                       jclass type)
{
    LOG_INFO("nativeOnPause");
    LOG_INFO("renderer = %p", renderer);
    renderer->stop();
}

extern "C" JNIEXPORT void JNICALL Java_vst_demo_opengl_addons_cube_NativeView_nativeOnStop(JNIEnv* jenv,
                                                                                      jclass type)
{
    LOG_INFO("nativeOnStop");
    LOG_INFO("renderer = %p", renderer);
    delete renderer;
    renderer = nullptr;
    LOG_INFO("renderer = %p", renderer);
}

extern "C" JNIEXPORT void JNICALL Java_vst_demo_opengl_addons_cube_NativeView_nativeSetSurface(JNIEnv* jenv,
                                                                                               jobject type, jobject surface)
{
    LOG_INFO("surface = %p", surface);
    if (surface != nullptr) {
        window = ANativeWindow_fromSurface(jenv, surface);
        LOG_INFO("Got window %p", window);
        LOG_INFO("renderer = %p", renderer);
        renderer->setWindow(window);
    } else {
        LOG_INFO("Releasing window");
        ANativeWindow_release(window);
    }
}