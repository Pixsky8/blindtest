#include "network/request.hh"

#include <curl/curl.h>
#include <curl/easy.h>
#include <stdexcept>
#include <string>

namespace network {
    static size_t
    WriteCallback(char *contents, size_t size, size_t nmemb, void *userp) {
        ((std::string *) userp)->append((char *) contents, size * nmemb);
        return size * nmemb;
    }

    static inline CURL *init_curl_request(const std::string &url,
                                          const std::string &cookie_jar,
                                          std::string &response) {
        CURL *curl = curl_easy_init();

        // Set URL of the request
        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());

        // Set cookie file of the request
        curl_easy_setopt(curl, CURLOPT_COOKIEFILE, cookie_jar.c_str());
        curl_easy_setopt(curl, CURLOPT_COOKIEJAR, cookie_jar.c_str());

        // Set response getter
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &response);

        return curl;
    }

    Request::Request(const std::string &url, const std::string &cookie_jar) {
        this->url = url;
        this->cookie_path = cookie_jar;
    }

    Request::~Request() {
        curl_slist_free_all(this->headers);
    }

    void Request::set_json_type() {
        this->headers =
            curl_slist_append(this->headers, "Content-Type: application/json");
    }

    void Request::set_method(const std::string &method) {
        this->method = method;
    }

    void Request::set_body(const std::string &body) {
        this->body = body;
    }

    std::string Request::perform() {
        std::string response;
        CURL *curl_request =
            init_curl_request(this->url, this->cookie_path, response);

        if (!this->body.empty()) {
            curl_easy_setopt(curl_request,
                             CURLOPT_POSTFIELDS,
                             this->body.c_str());
        }

        if (!this->method.empty()) {
            curl_easy_setopt(curl_request,
                             CURLOPT_CUSTOMREQUEST,
                             this->method.c_str());
        }

        if (this->headers)
            curl_easy_setopt(curl_request, CURLOPT_HTTPHEADER, this->headers);

        this->response_code = curl_easy_perform(curl_request);

        curl_easy_cleanup(curl_request);

        return response;
    }

    Request login_request(const std::string &hostname,
                          const std::string &cookie_path,
                          const std::string &username,
                          const std::string &passwd) {
        std::string host = hostname + "/api/login";
        Request res(host, cookie_path);

        if (username.find("\"") != std::string::npos ||
            passwd.find("\"") != std::string::npos) {
            throw std::invalid_argument(
                "Username and password cannot have a \" inside.");
        }

        res.set_body(std::string("{\"login\": \"") + username +
                     "\",\"password\":\"" + passwd + "\"}");
        res.set_json_type();
        res.set_method("POST");

        return res;
    }

    Request answer_request(const std::string &hostname,
                           const std::string &cookie_path) {
        std::string host = hostname + "/api/answer";
        Request res(host, cookie_path);

        res.set_method("GET");

        return res;
    }
} // namespace network
