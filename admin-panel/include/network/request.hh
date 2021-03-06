#pragma once

#include <curl/curl.h>
#include <string>

namespace network {
    class Request {
    private:
        std::string url;
        std::string method;
        std::string cookie_path;
        struct curl_slist *headers = NULL;
        std::string body;

        int response_code = -1;

    public:
        Request(const std::string &url, const std::string &cookie_jar);

        ~Request();

        int get_response_code() {
            return response_code;
        }

        void set_method(const std::string &method);
        void set_json_type();
        void set_body(const std::string &body);

        std::string perform();
    };

    Request login_request(const std::string &hostname,
                          const std::string &cookie_path,
                          const std::string &username,
                          const std::string &passwd);

    Request answer_request(const std::string &hostname,
                           const std::string &cookie_path);

    Request question_request(const std::string &hostname,
                             const std::string &cookie_path,
                             int question_id);

    Request profile_get_request(const std::string &hostname,
                                const std::string &cookie_path);

    Request give_points_request(const std::string &hostname,
                                const std::string &cookie_path,
                                const std::string &player_name,
                                int points);
} // namespace network
