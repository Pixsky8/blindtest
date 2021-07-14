#include <QLabel>
#include <QLineEdit>
#include <QMessageBox>
#include <QPushButton>
#include <QString>
#include <QVBoxLayout>
#include <QWidget>

#include <iostream>

#include "config.hh"
#include "network/request.hh"

namespace interface {
    void send_login(QLineEdit *username_input, QLineEdit *password_input) {
        auto request =
            network::login_request(g_config.host_name,
                                   g_config.cookie_file,
                                   username_input->text().toStdString(),
                                   password_input->text().toStdString());

        QMessageBox response;
        response.setText(QString::fromUtf8(request.perform().c_str()));
        response.exec();
    }

    QWidget *create_login_page() {
        auto login_page = new QWidget;

        auto username_layout = new QVBoxLayout;
        auto username_lbl = new QLabel("Username:");
        auto username_input = new QLineEdit;
        username_layout->addWidget(username_lbl);
        username_layout->addWidget(username_input);
        username_layout->setContentsMargins(QMargins(0, 0, 0, 15));

        auto password_layout = new QVBoxLayout;
        auto password_lbl = new QLabel("Password:");
        auto password_input = new QLineEdit;
        password_input->setEchoMode(QLineEdit::Password);
        password_layout->addWidget(password_lbl);
        password_layout->addWidget(password_input);
        password_layout->setContentsMargins(QMargins(0, 0, 0, 15));

        auto login_button = new QPushButton("Login");
        QObject::connect(login_button, &QPushButton::released, [=]() {
            send_login(username_input, password_input);
        });

        QVBoxLayout *login_layout = new QVBoxLayout;
        login_layout->addLayout(username_layout);
        login_layout->addLayout(password_layout);
        login_layout->addWidget(login_button);
        login_layout->addStretch(1);

        login_page->setLayout(login_layout);

        return login_page;
    }
} // namespace interface
