#include <QApplication>
#include <QPushButton>
#include <curl/curl.h>
#include <curl/easy.h>
#include <curl/urlapi.h>
#include <iostream>
#include <string>

#include "interface/window.hh"
#include "network/request.hh"

int main(int argc, char *argv[]) {
    /*
    std::string username = "admin";
    std::string passwd = "minad";
    auto request = network::login_request("http://localhost",
                                          "/tmp/cookie.txt",
                                          "admin",
                                          "minad");

    std::cout << request.perform() << std::endl;
    std::cout << request.get_response_code() << std::endl;
    */

    QApplication app(argc, argv);

    QMainWindow *main_window = interface::create_window();
    main_window->show();

    int res = app.exec();
    delete main_window;
    return res;
}
