# CMAKE SETUP
cmake_minimum_required(VERSION 3.12)
project(admin-panel)

set(CUSTOM_FLAGS
  "-g"
)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra ${CUSTOM_FLAGS}")
# ! CMAKE SETUP

# SOURCES
set(SRC
  "source/interface/login.cc"
  "source/interface/window.cc"
  "source/network/request.cc"
)

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
target_link_libraries(admin-panel PRIVATE ${LIBRARIES} "-lcurl")
