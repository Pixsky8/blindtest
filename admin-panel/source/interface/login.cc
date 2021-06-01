#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QVBoxLayout>
#include <QWidget>

#include <iostream>

namespace interface {
    void send_login(QLineEdit *username_input, QLineEdit *password_input) {
        std::cout << username_input->text().toStdString() << std::endl;
        std::cout << password_input->text().toStdString() << std::endl;
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
