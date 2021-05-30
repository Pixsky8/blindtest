# CMAKE SETUP
cmake_minimum_required(VERSION 3.12)
project(admin-panel)

set(CMAKE_CXX_STANDARD 17)
# ! CMAKE SETUP

# SOURCES
set(SRC)

include_directories("include")
# ! SOURCES

# LIBRARIES
find_package(Qt5 COMPONENTS Widgets REQUIRED)
set(LIBRARIES
    Qt5::Widgets
)
# ! LIBRARIES

add_executable(admin-panel)
target_sources(admin-panel PRIVATE "source/main.cc" ${SRC})
target_link_libraries(admin-panel PRIVATE ${LIBRARIES})
