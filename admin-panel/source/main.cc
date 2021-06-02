#include <QApplication>
#include <QPushButton>
#include <curl/curl.h>
#include <curl/easy.h>
#include <curl/urlapi.h>
#include <iostream>
#include <string>

#include "config.hh"
#include "interface/window.hh"
#include "network/request.hh"

int main(int argc, char *argv[]) {
    QApplication app(argc, argv);

    QMainWindow *main_window = interface::create_window();
    main_window->show();

    int res = app.exec();
    delete main_window;
    return res;
}
