#include <QApplication>
#include <QPushButton>
#include <qmainwindow.h>

#include "interface/window.hh"

int main(int argc, char *argv[]) {
    QApplication app(argc, argv);

    QMainWindow *main_window = interface::create_window();
    main_window->show();

    int res = app.exec();
    delete main_window;
    return res;
}
