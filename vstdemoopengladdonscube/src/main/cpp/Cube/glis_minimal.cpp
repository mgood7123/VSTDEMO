//
// Created by smallville7123 on 29/07/20.
//

#include "glis_minimal.h"

std::string GLIS_INTERNAL_MESSAGE_PREFIX = "";

bool GLIS_LOG_PRINT_NON_ERRORS = false;

#define GLIS_SWITCH_CASE_CUSTOM_CASE_CUSTOM_LOGGER_CUSTOM_STRING_CAN_I_PRINT_ERROR(LOGGING_FUNCTION, CASE_NAME, name, const, constSTRING, UNNAMED_STRING_CAN_PRINT_ERROR, UNNAMED_STRING_CANNOT_PRINT_ERROR, NAMED_STRING_CAN_PRINT_ERROR, NAMED_STRING_CANNOT_PRINT_ERROR, PRINT, IS_AN_ERROR) CASE_NAME: { \
    if(name == nullptr || name == nullptr || name == 0) { \
        if (PRINT) { \
            if ((UNNAMED_STRING_CAN_PRINT_ERROR) != nullptr) { \
                std::string msg = GLIS_INTERNAL_MESSAGE_PREFIX; \
                msg += UNNAMED_STRING_CAN_PRINT_ERROR; \
                LOGGING_FUNCTION(msg.c_str(), constSTRING); \
            } \
        } \
        else { \
            if ((UNNAMED_STRING_CANNOT_PRINT_ERROR) != nullptr) { \
                std::string msg = GLIS_INTERNAL_MESSAGE_PREFIX; \
                msg += UNNAMED_STRING_CANNOT_PRINT_ERROR; \
                LOGGING_FUNCTION("%s", msg.c_str()); \
            } \
        } \
    } \
    else { \
        if (PRINT) { \
            if ((NAMED_STRING_CAN_PRINT_ERROR) != nullptr) { \
                std::string msg = GLIS_INTERNAL_MESSAGE_PREFIX; \
                msg += NAMED_STRING_CAN_PRINT_ERROR; \
                LOGGING_FUNCTION(msg.c_str(), name, constSTRING); \
            } \
        } \
        else { \
            if ((NAMED_STRING_CANNOT_PRINT_ERROR) != nullptr) { \
                std::string msg = GLIS_INTERNAL_MESSAGE_PREFIX; \
                msg += NAMED_STRING_CANNOT_PRINT_ERROR; \
                LOGGING_FUNCTION(msg.c_str(), name); \
            } \
        } \
    } \
    if (IS_AN_ERROR) abort(); \
    break; \
}


#define GLIS_SWITCH_CASE_CUSTOM_LOGGER_CUSTOM_STRING_DONT_PRINT_ERROR(LOGGER, name, const, constSTRING, UNNAMED_STRING, NAMED_STRING) \
    GLIS_SWITCH_CASE_CUSTOM_CASE_CUSTOM_LOGGER_CUSTOM_STRING_CAN_I_PRINT_ERROR(LOGGER, case const, name, const, constSTRING, nullptr, UNNAMED_STRING, nullptr, NAMED_STRING, false, false)

#define GLIS_SWITCH_CASE_CUSTOM_LOGGER_CUSTOM_STRING(LOGGER, name, const, constSTRING, UNNAMED_STRING, NAMED_STRING, IS_AN_ERROR) \
    GLIS_SWITCH_CASE_CUSTOM_CASE_CUSTOM_LOGGER_CUSTOM_STRING_CAN_I_PRINT_ERROR(LOGGER, case const, name, const, constSTRING, UNNAMED_STRING, nullptr, NAMED_STRING, nullptr, true, IS_AN_ERROR)

#define GLIS_ERROR_SWITCH_CASE_CUSTOM_STRING(name, const, constSTRING, UNNAMED_STRING, NAMED_STRING) \
    GLIS_SWITCH_CASE_CUSTOM_LOGGER_CUSTOM_STRING(LOG_ERROR, name, const, constSTRING, UNNAMED_STRING, NAMED_STRING, true)

#define GLIS_ERROR_SWITCH_CASE(name, const) \
    GLIS_ERROR_SWITCH_CASE_CUSTOM_STRING(name, const, #const, "%s", "%s generated error: %s")

#define GLIS_ERROR_SWITCH_CASE_DEFAULT(name, err) \
    GLIS_SWITCH_CASE_CUSTOM_CASE_CUSTOM_LOGGER_CUSTOM_STRING_CAN_I_PRINT_ERROR(LOG_ERROR, default, name, err, err, "Unknown error: %d", "Unknown error", "%s generated an unknown error: %d", "%s generated an unknown error", true, true)


#include <stdarg.h>
#include <android/log.h>
#include <mutex>
#include <GLES3/gl32.h>
#include <android/native_window.h>

std::mutex lock;

int LOG_INFO(const char* format, ... ) {
    assert(format != nullptr);
    lock.lock();
#ifdef __ANDROID__
    va_list args2;
    va_start(args2, format);
    __android_log_vprint(ANDROID_LOG_INFO, "GLIS", format, args2);
    va_end(args2);
#endif
    // set color to green
    fprintf(stdout, "\033[0;32m");
    va_list args;
    va_start(args, format);
    int len = vfprintf(stdout, format, args);
    va_end(args);
    // clear color
    fprintf(stdout, "\033[0m");
    len += fprintf(stdout, "\n");
    fflush(stdout);
    lock.unlock();
    return len;
}

int LOG_ERROR(const char* format, ... ) {
    assert(format != nullptr);
    lock.lock();
#ifdef __ANDROID__
    va_list args2;
    va_start(args2, format);
    __android_log_vprint(ANDROID_LOG_ERROR, "GLIS", format, args2);
    va_end(args2);
#endif
    // set color to red
    fprintf(stderr, "\033[0;31m");
    va_list args;
    va_start(args, format);
    int len = vfprintf(stderr, format, args);
    va_end(args);
    // clear color
    fprintf(stderr, "\033[0m");
    len += fprintf(stderr, "\n");
    fflush(stderr);
    lock.unlock();
    return len;
}

void LOG_ALWAYS_FATAL(const char* format, ... ) {
    assert(format != nullptr);
    lock.lock();
#ifdef __ANDROID__
    va_list args2;
    va_start(args2, format);
    __android_log_vprint(ANDROID_LOG_ERROR, "GLIS", format, args2);
    va_end(args2);
#endif
    // set color to red
    fprintf(stderr, "\033[0;31m");
    va_list args;
    va_start(args, format);
    vfprintf(stderr, format, args);
    va_end(args);
    // clear color
    fprintf(stderr, "\033[0m");
    fprintf(stderr, "\n");
    fflush(stderr);
    lock.unlock();
    abort();
}

void GLIS_error_to_string_GL(const char *name, GLint err) {
    GLIS_INTERNAL_MESSAGE_PREFIX = "OpenGL:          ";
    switch (err) {
        // GENERATED BY glGetError() ITSELF
        GLIS_SWITCH_CASE_CUSTOM_LOGGER_CUSTOM_STRING_DONT_PRINT_ERROR(LOG_INFO, name,
                                                                      GL_NO_ERROR,
                                                                      "GL_NO_ERROR",
                                                                      GLIS_LOG_PRINT_NON_ERRORS
                                                                      ? "no error was generated"
                                                                      : nullptr,
                                                                      GLIS_LOG_PRINT_NON_ERRORS
                                                                      ? "%s did not generate an error"
                                                                      : nullptr)
        GLIS_ERROR_SWITCH_CASE(name, GL_INVALID_ENUM)
        GLIS_ERROR_SWITCH_CASE(name, GL_INVALID_VALUE)
        GLIS_ERROR_SWITCH_CASE(name, GL_INVALID_OPERATION)
        GLIS_ERROR_SWITCH_CASE(name, GL_OUT_OF_MEMORY)

        // WHEN ALL ELSE FAILS
        GLIS_ERROR_SWITCH_CASE_DEFAULT(name, err)
    }
    GLIS_INTERNAL_MESSAGE_PREFIX = "";
}

void GLIS_error_to_string_EGL(const char *name, EGLint err) {
    GLIS_INTERNAL_MESSAGE_PREFIX = "OpenGL ES (EGL): ";
    switch (err) {
        // GENERATED BY eglGetError() ITSELF
        GLIS_SWITCH_CASE_CUSTOM_LOGGER_CUSTOM_STRING_DONT_PRINT_ERROR(LOG_INFO, name,
                                                                      EGL_SUCCESS,
                                                                      "EGL_SUCCESS",
                                                                      GLIS_LOG_PRINT_NON_ERRORS
                                                                      ? "no error was generated"
                                                                      : nullptr,
                                                                      GLIS_LOG_PRINT_NON_ERRORS
                                                                      ? "%s did not generate an error"
                                                                      : nullptr)
        GLIS_ERROR_SWITCH_CASE(name, EGL_NOT_INITIALIZED)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_ACCESS)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_ALLOC)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_ATTRIBUTE)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_CONTEXT)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_CONFIG)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_CURRENT_SURFACE)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_DISPLAY)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_SURFACE)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_MATCH)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_PARAMETER)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_NATIVE_PIXMAP)
        GLIS_ERROR_SWITCH_CASE(name, EGL_BAD_NATIVE_WINDOW)
        GLIS_ERROR_SWITCH_CASE(name, EGL_CONTEXT_LOST)

        // WHEN ALL ELSE FAILS
        GLIS_ERROR_SWITCH_CASE_DEFAULT(name, err)
    }
    GLIS_INTERNAL_MESSAGE_PREFIX = "";
}

void GLIS_error_to_string_GL(GLint err) {
    GLIS_error_to_string_GL(nullptr, err);
}

void GLIS_error_to_string_EGL(EGLint err) {
    GLIS_error_to_string_EGL(nullptr, err);
}

void GLIS_error_to_string_GL() {
    GLIS_error_to_string_GL(glGetError());
}

void GLIS_error_to_string_EGL() {
    GLIS_error_to_string_EGL(eglGetError());
}

void GLIS_error_to_string() {
    GLIS_error_to_string_GL();
    GLIS_error_to_string_EGL();
}

void GLIS_error_to_string_GL(const char *name) {
    GLIS_error_to_string_GL(name, glGetError());
}

void GLIS_error_to_string_EGL(const char *name) {
    GLIS_error_to_string_EGL(name, eglGetError());
}

void GLIS_error_to_string(const char *name) {
    GLIS_error_to_string_GL(name);
    GLIS_error_to_string_EGL(name);
}

void GLIS_GL_INFORMATION() {
    const GLubyte *vendor = glGetString(GL_VENDOR);
    const GLubyte *renderer = glGetString(GL_RENDERER);
    const GLubyte *version = glGetString(GL_VERSION);
    const GLubyte *slv = glGetString(GL_SHADING_LANGUAGE_VERSION);
    const GLubyte *extentions = glGetString(GL_EXTENSIONS);
    LOG_INFO("GL_VENDOR: %s", vendor);
    LOG_INFO("GL_RENDERER: %s", renderer);
    LOG_INFO("GL_VERSION: %s", version);
    LOG_INFO("GL_SHADING_LANGUAGE_VERSION: %s", slv);
    LOG_INFO("GL_EXTENSIONS: %s", extentions);
}

void GLIS_EGL_INFORMATION(EGLDisplay &DISPLAY) {
    const char *client_apis = eglQueryString(DISPLAY, EGL_CLIENT_APIS);
    const char *vendor = eglQueryString(DISPLAY, EGL_VENDOR);
    const char *version = eglQueryString(DISPLAY, EGL_VERSION);
    const char *extentions = eglQueryString(DISPLAY, EGL_EXTENSIONS);
    LOG_INFO("EGL_CLIENT_APIS: %s", client_apis);
    LOG_INFO("EGL_VENDOR: %s", vendor);
    LOG_INFO("EGL_VERSION: %s", version);
    LOG_INFO("EGL_EXTENSIONS: %s", extentions);
}

bool GLIS_initialize_display(class GLIS_CLASS &GLIS) {
    GLIS.display = eglGetDisplay(GLIS.display_id);
    GLIS_error_to_string_EGL("eglGetDisplay");
    if (GLIS.display == EGL_NO_DISPLAY) return false;
    GLIS.init_eglGetDisplay = true;
    EGLBoolean r = eglInitialize(GLIS.display, &GLIS.eglMajVers, &GLIS.eglMinVers);
    GLIS_error_to_string_EGL("eglInitialize");
    if (r == EGL_FALSE) return false;
    GLIS.init_eglInitialize = true;
    LOG_INFO("EGL initialized with version %d.%d", GLIS.eglMajVers, GLIS.eglMinVers);
    GLIS_EGL_INFORMATION(GLIS.display);
    return true;
}

bool GLIS_initialize_configuration(class GLIS_CLASS &GLIS) {
    EGLBoolean r = eglChooseConfig(GLIS.display, GLIS.configuration_attributes, &GLIS.configuration, 1, &GLIS.number_of_configurations);
    GLIS_error_to_string_EGL("eglChooseConfig");
    if (r == EGL_FALSE) return false;
    GLIS.init_eglChooseConfig = true;
    return true;
}

bool GLIS_initialize_surface_CreateWindowSurface(class GLIS_CLASS &GLIS) {
    if(GLIS.native_window == 0) {
        const char * msg = "error: a native window must be supplied prior to calling this function";
        LOG_ALWAYS_FATAL("%s", msg);
    }
    GLIS.surface = eglCreateWindowSurface(GLIS.display, GLIS.configuration, GLIS.native_window, nullptr);
    GLIS_error_to_string_EGL("eglCreateWindowSurface");
    if (GLIS.surface == EGL_NO_SURFACE) return false;
    GLIS.init_eglCreateWindowSurface = true;
    return true;
}

bool GLIS_initialize_surface_CreatePbufferSurface(class GLIS_CLASS &GLIS) {
    GLIS.surface = eglCreatePbufferSurface(GLIS.display, GLIS.configuration, GLIS.surface_attributes);
    GLIS_error_to_string_EGL("eglCreatePbufferSurface");
    if (GLIS.surface == EGL_NO_SURFACE) return false;
    GLIS.init_eglCreatePbufferSurface = true;
    return true;
}

bool GLIS_create_context(class GLIS_CLASS &GLIS) {
    GLIS.context = eglCreateContext(GLIS.display, GLIS.configuration, GLIS.shared_context, GLIS.context_attributes);
    GLIS_error_to_string_EGL("eglCreateContext");
    if (GLIS.context == EGL_NO_CONTEXT) return false;
    GLIS.init_eglCreateContext = true;
    return true;
}

bool GLIS_switch_to_context(class GLIS_CLASS &GLIS) {
    EGLBoolean r = eglMakeCurrent(GLIS.display, GLIS.surface, GLIS.surface, GLIS.context);
    GLIS_error_to_string_EGL("eglMakeCurrent");
    if (r == EGL_FALSE) return false;
    GLIS.init_eglMakeCurrent = true;
    GLIS_GL_INFORMATION();
    return true;
}

bool GLIS_get_width_height(class GLIS_CLASS &GLIS) {
    EGLBoolean r1 = eglQuerySurface(GLIS.display, GLIS.surface, EGL_WIDTH, &GLIS.width);
    GLIS_error_to_string_EGL("eglQuerySurface");
    if (r1 == EGL_FALSE) return false;
    EGLBoolean r2 = eglQuerySurface(GLIS.display, GLIS.surface, EGL_HEIGHT, &GLIS.height);
    GLIS_error_to_string_EGL("eglQuerySurface");
    if (r2 == EGL_FALSE) return false;
    return true;
}

bool GLIS_initialize(class GLIS_CLASS &GLIS, GLint surface_type, bool debug) {
    if (GLIS.init_GLIS) return true;

    LOG_INFO("Initializing");
    EGLBoolean r = eglBindAPI(EGL_OPENGL_ES_API);
    GLIS_error_to_string_EGL("eglBindAPI");
    if (r == EGL_FALSE) {
        return false;
    }

    LOG_INFO("Initializing display");
    if (!GLIS_initialize_display(GLIS)) {
        LOG_ERROR("Failed to initialize display");
        GLIS_destroy_GLIS(GLIS);
        return false;
    }
    LOG_INFO("Initialized display");

//    if (debug) {
//        LOG_INFO("Debug mode enabled");
//        const EGLint context_attributes[] = {
//                EGL_CONTEXT_CLIENT_VERSION, 3, EGL_CONTEXT_FLAGS_KHR, EGL_CONTEXT_OPENGL_DEBUG_BIT_KHR, EGL_NONE
//        };
//        GLIS.context_attributes = context_attributes;
//        GLIS.debug_context = true;
//    } else {
        LOG_INFO("Debug mode disabled");
        const EGLint context_attributes[] = {EGL_CONTEXT_CLIENT_VERSION, GLIS.eglMajVers, EGL_NONE};
        GLIS.context_attributes = context_attributes;
//    }

    LOG_INFO("Initializing configuration");
    if (!GLIS_initialize_configuration(GLIS)) {
        LOG_ERROR("Failed to initialize configuration");
        GLIS_destroy_GLIS(GLIS);
        return false;
    }
    LOG_INFO("Initialized configuration");
    GLint format;
    if (!eglGetConfigAttrib(GLIS.display, GLIS.configuration, EGL_NATIVE_VISUAL_ID, &format)) {
        LOG_ERROR("Failed to initialize obtain buffer format");
        GLIS_destroy_GLIS(GLIS);
        return false;
    }

    if (ANativeWindow_setBuffersGeometry(GLIS.native_window, 0, 0, format) != 0) {
        LOG_ERROR("Failed to set geometry");
        GLIS_destroy_GLIS(GLIS);
        return false;
    }

    LOG_INFO("Initializing surface");
    if (surface_type == EGL_WINDOW_BIT) {
        LOG_INFO("Creating window surface");
        if (!GLIS_initialize_surface_CreateWindowSurface(GLIS)) {
            LOG_ERROR("Failed to initialize surface");
            GLIS_destroy_GLIS(GLIS);
            return false;
        }
    } else if (surface_type == EGL_PBUFFER_BIT) {
        LOG_INFO("creating pixel buffer surface");
        if (!GLIS_initialize_surface_CreatePbufferSurface(GLIS)) {
            LOG_ERROR("Failed to initialize surface");
            GLIS_destroy_GLIS(GLIS);
            return false;
        }
    }
    LOG_INFO("Initialized surface");
    LOG_INFO("Initializing context");
    if (!GLIS_create_context(GLIS)) {
        LOG_ERROR("Failed to initialize context");
        GLIS_destroy_GLIS(GLIS);
        return false;
    }
    LOG_INFO("Initialized context");
    LOG_INFO("Switching to context");
    if (!GLIS_switch_to_context(GLIS)) {
        LOG_ERROR("Failed to switch to context");
        GLIS_destroy_GLIS(GLIS);
        return false;
    }
    LOG_INFO("Switched to context");
    // Magnum does not support glDebugMessageCallback
//    if (debug) {
//        LOG_INFO("Enabling debug callbacks");
//        enable_debug_callbacks();
//        LOG_INFO("Enabled debug callbacks");
//        GLIS.init_debug = true;
//    }
    LOG_INFO("Obtaining surface width and height");
    if (!GLIS_get_width_height(GLIS)) {
        LOG_ERROR("Failed to obtain surface width and height");
        GLIS_destroy_GLIS(GLIS);
        return false;
    }
    LOG_INFO("Obtained surface width and height");
    GLIS.init_GLIS = true;
    LOG_INFO("Initialized");
    return true;
}

void GLIS_destroy_GLIS(class GLIS_CLASS &GLIS) {
    if (!GLIS.init_GLIS) return;
    LOG_INFO("Uninitializing");

//    if (GLIS.init_debug) {
//        LOG_INFO("Disabling debug callbacks");
//        disable_debug_callbacks();
//        LOG_INFO("Disabled debug callbacks");
//        GLIS.init_debug = false;
//    }
    if (GLIS.init_eglMakeCurrent) {
        LOG_INFO("Switching context to no context");
        eglMakeCurrent(GLIS.display, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
        GLIS_error_to_string_EGL("eglMakeCurrent");
        GLIS.init_eglMakeCurrent = false;
    }
    if (GLIS.init_eglCreateContext) {
        LOG_INFO("Uninitializing context");
        eglDestroyContext(GLIS.display, GLIS.context);
        GLIS_error_to_string_EGL("eglDestroyContext");
        GLIS.context = EGL_NO_CONTEXT;
        GLIS.shared_context = EGL_NO_CONTEXT;
        GLIS.init_eglCreateContext = false;
    }
    if (GLIS.init_eglCreateWindowSurface || GLIS.init_eglCreatePbufferSurface) {
        LOG_INFO("Uninitializing surface");
        eglDestroySurface(GLIS.display, GLIS.surface);
        GLIS_error_to_string_EGL("eglDestroySurface");
        GLIS.surface = EGL_NO_SURFACE;
        GLIS.init_eglCreateWindowSurface = false;
        GLIS.init_eglCreatePbufferSurface = false;
    }
    if (GLIS.init_eglChooseConfig) {
        // TODO: figure how to undo init_eglChooseConfig
    }
    if (GLIS.init_eglInitialize) {
        LOG_INFO("Uninitializing display");
        eglTerminate(GLIS.display);
        GLIS_error_to_string_EGL("eglTerminate");
        GLIS.init_eglInitialize = false;
    }
    if (GLIS.init_eglGetDisplay) {
        LOG_INFO("Setting display to no display");
        GLIS.display = EGL_NO_DISPLAY;
        GLIS.init_eglGetDisplay = false;
    }
    GLIS.init_GLIS = false;
    LOG_INFO("Uninitialized");
}

bool GLIS_setupOnScreenRendering(class GLIS_CLASS &GLIS, EGLContext shared_context) {
    GLIS.shared_context = shared_context;

    const EGLint config[] = {EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT, EGL_NONE};
    GLIS.configuration_attributes = config;

    const EGLint surface[] = {EGL_RENDERABLE_TYPE, EGL_OPENGL_ES3_BIT, EGL_BLUE_SIZE, 8,
                              EGL_GREEN_SIZE, 8, EGL_RED_SIZE, 8, EGL_ALPHA_SIZE, 8,
                              EGL_DEPTH_SIZE, 16, EGL_NONE};
    GLIS.surface_attributes = surface;

    return GLIS_initialize(GLIS, EGL_WINDOW_BIT, true);
}

bool GLIS_setupOnScreenRendering(class GLIS_CLASS &GLIS) {
    return GLIS_setupOnScreenRendering(GLIS, EGL_NO_CONTEXT);
}
