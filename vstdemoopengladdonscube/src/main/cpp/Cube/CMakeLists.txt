cmake_minimum_required(VERSION 3.4.1)

add_library(cube SHARED jniapi.cpp renderer.cpp glis_minimal.cpp)
target_link_libraries(cube android log EGL GLESv3)