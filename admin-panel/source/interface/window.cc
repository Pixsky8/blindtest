#include <QMainWindow>
#include <QTabWidget>
#include <memory>

namespace interface {
    QMainWindow *create_window() {
        auto window = new QMainWindow;
        window->setWindowTitle("Admin Panel");

        auto tabs = new QTabWidget;

        auto anwsers_page = new QWidget;
        auto login_page = new QWidget;
        tabs->addTab(anwsers_page, "Anwsers");
        tabs->addTab(login_page, "Login");

        window->setMenuWidget(tabs);

        return window;
    }
} // namespace interface
