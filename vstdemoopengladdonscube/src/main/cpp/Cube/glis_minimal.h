//
// Created by smallville7123 on 29/07/20.
//

#ifndef VST_DEMO_GLIS_MINIMAL_H
#define VST_DEMO_GLIS_MINIMAL_H

#include <string>
#include <EGL/egl.h> // requires ndk r5 or newer
#include <GLES/gl.h>

class GLIS_CLASS {
public:
    int init_GLIS = false;
    bool
            init_eglGetDisplay = false,
            init_eglInitialize = false,
            init_eglChooseConfig = false,
            init_eglCreateWindowSurface = false,
            init_eglCreatePbufferSurface = false,
            init_eglCreateContext = false,
            init_eglMakeCurrent = false,
            init_debug = false;
    const GLint
            *configuration_attributes = nullptr,
            *context_attributes = nullptr,
            *surface_attributes = nullptr;
    EGLint
            eglMajVers = 0,
            eglMinVers = 0,
            number_of_configurations = 0;
    EGLNativeDisplayType display_id = EGL_DEFAULT_DISPLAY;
    EGLDisplay display = EGL_NO_DISPLAY;
    EGLConfig configuration = 0;
    EGLContext
            context = EGL_NO_CONTEXT,
            shared_context = EGL_NO_CONTEXT;
    EGLSurface surface = EGL_NO_SURFACE;
    EGLNativeWindowType native_window = 0;
    GLint
            width = 0,
            height = 0;
};

extern bool GLIS_LOG_PRINT_NON_ERRORS;

static void GLIS_error_to_string_GL(const char *name, GLint err);

static void GLIS_error_to_string_EGL(const char *name, EGLint err);

static void GLIS_error_to_string_GL(GLint err);

static void GLIS_error_to_string_EGL(EGLint err);

static void GLIS_error_to_string_GL();

static void GLIS_error_to_string_EGL();

static void GLIS_error_to_string();

static void GLIS_error_to_string_GL(const char *name);

void GLIS_error_to_string_EGL(const char *name);

static void GLIS_error_to_string(const char *name);

static void GLIS_GL_INFORMATION();

static void GLIS_EGL_INFORMATION(EGLDisplay &DISPLAY);

void GLIS_destroy_GLIS(class GLIS_CLASS &GLIS);

bool GLIS_initialize_display(class GLIS_CLASS &GLIS);

bool GLIS_initialize_configuration(class GLIS_CLASS &GLIS);

bool GLIS_initialize_surface_CreateWindowSurface(class GLIS_CLASS &GLIS);

bool GLIS_initialize_surface_CreatePbufferSurface(class GLIS_CLASS &GLIS);

bool GLIS_create_context(class GLIS_CLASS &GLIS);

bool GLIS_switch_to_context(class GLIS_CLASS &GLIS);

bool GLIS_get_width_height(class GLIS_CLASS &GLIS);

bool GLIS_initialize(class GLIS_CLASS &GLIS, GLint surface_type, bool debug);

bool GLIS_setupOnScreenRendering(class GLIS_CLASS &GLIS, EGLContext shared_context);

bool GLIS_setupOnScreenRendering(class GLIS_CLASS &GLIS);

#endif //VST_DEMO_GLIS_MINIMAL_H
