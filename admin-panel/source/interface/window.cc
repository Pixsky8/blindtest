#include <QLabel>
#include <QLineEdit>
#include <QMainWindow>
#include <QPushButton>
#include <QTabWidget>
#include <QTextEdit>
#include <QVBoxLayout>
#include <memory>

#include "interface/answer.hh"
#include "interface/config.hh"
#include "interface/login.hh"

namespace interface {
    QMainWindow *create_window() {
        auto window = new QMainWindow;
        window->setWindowTitle("Admin Panel");

        auto tabs = new QTabWidget();
        tabs->setSizePolicy(QSizePolicy::MinimumExpanding,
                            QSizePolicy::MinimumExpanding);

        auto anwsers_page = new AnswerWidget;
        auto login_page = create_login_page();
        tabs->addTab(anwsers_page, "Anwsers");
        tabs->addTab(login_page, "Login");
        tabs->addTab(create_option_page(), "Option");
        tabs->setCurrentIndex(1);

        window->setCentralWidget(tabs);

        return window;
    }
} // namespace interface
