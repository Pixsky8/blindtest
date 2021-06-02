#include <QLabel>
#include <QLineEdit>
#include <QMainWindow>
#include <QPushButton>
#include <QTabWidget>
#include <QVBoxLayout>
#include <memory>

#include "config.hh"
#include "interface/login.hh"

namespace interface {
    static QWidget *create_answers_page() {
        auto answers_page = new QWidget;

        return answers_page;
    }

    static QWidget *create_option_page() {
        auto option_page = new QWidget;

        auto hostname_lbl = new QLabel("Hostname:");
        auto cookie_lbl = new QLabel("Cookie file:");
        auto hostname_input = new QLineEdit;
        auto cookie_input = new QLineEdit;

        hostname_input->setText(QString::fromUtf8(g_config.host_name.c_str()));
        cookie_input->setText(QString::fromUtf8(g_config.cookie_file.c_str()));

        auto save_button = new QPushButton("Save");
        QObject::connect(save_button, &QPushButton::released, [=]() {
            g_config.host_name = hostname_input->text().toStdString();
            g_config.cookie_file = cookie_input->text().toStdString();
        });

        QVBoxLayout *option_layout = new QVBoxLayout;
        option_layout->addWidget(hostname_lbl);
        option_layout->addWidget(hostname_input);
        option_layout->addWidget(cookie_lbl);
        option_layout->addWidget(cookie_input);
        option_layout->addWidget(save_button);
        option_layout->addStretch(1);

        option_page->setLayout(option_layout);

        return option_page;
    }

    QMainWindow *create_window() {
        auto window = new QMainWindow;
        window->setWindowTitle("Admin Panel");

        auto tabs = new QTabWidget;

        auto anwsers_page = create_answers_page();
        auto login_page = create_login_page();
        tabs->addTab(anwsers_page, "Anwsers");
        tabs->addTab(login_page, "Login");
        tabs->addTab(create_option_page(), "Option");

        window->setMenuWidget(tabs);

        return window;
    }
} // namespace interface
