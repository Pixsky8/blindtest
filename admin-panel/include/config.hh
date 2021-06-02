#pragma once

#include <string>

class Config {
  public:
    Config(const std::string &host_name_, const std::string &cookie_file_)
        : host_name(host_name_)
        , cookie_file(cookie_file_) {
    }

    std::string host_name;
    std::string cookie_file;
};

extern Config g_config;
