#include <QLabel>
#include <QLineEdit>
#include <QMainWindow>
#include <QPushButton>
#include <QTabWidget>
#include <QVBoxLayout>
#include <memory>

#include "interface/login.hh"

namespace interface {
    static QWidget *create_answers_page() {
        auto answers_page = new QWidget;

        return answers_page;
    }

    QMainWindow *create_window() {
        auto window = new QMainWindow;
        window->setWindowTitle("Admin Panel");

        auto tabs = new QTabWidget;

        auto anwsers_page = create_answers_page();
        auto login_page = create_login_page();
        tabs->addTab(anwsers_page, "Anwsers");
        tabs->addTab(login_page, "Login");

        window->setMenuWidget(tabs);

        return window;
    }
} // namespace interface
