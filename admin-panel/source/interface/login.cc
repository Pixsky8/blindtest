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

        auto username_lbl = new QLabel("Username:");
        auto password_lbl = new QLabel("Password:");
        auto username_input = new QLineEdit;
        auto password_input = new QLineEdit;
        password_input->setEchoMode(QLineEdit::Password);

        auto login_button = new QPushButton("Login");
        QObject::connect(login_button, &QPushButton::released, [=]() {
            send_login(username_input, password_input);
        });

        QVBoxLayout *login_layout = new QVBoxLayout;
        login_layout->addWidget(username_lbl);
        login_layout->addWidget(username_input);
        login_layout->addWidget(password_lbl);
        login_layout->addWidget(password_input);
        login_layout->addWidget(login_button);
        login_layout->addStretch(1);

        login_page->setLayout(login_layout);

        return login_page;
    }
} // namespace interface
