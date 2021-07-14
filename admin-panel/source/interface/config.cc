#include <QLabel>
#include <QLineEdit>
#include <QPushButton>
#include <QVBoxLayout>
#include <QWidget>

#include "config.hh"

namespace interface {
    QWidget *create_option_page() {
        auto option_page = new QWidget;

        auto hostname_layout = new QVBoxLayout;
        auto hostname_lbl = new QLabel("Hostname:");
        auto hostname_input = new QLineEdit;
        hostname_input->setText(QString::fromUtf8(g_config.host_name.c_str()));
        hostname_layout->addWidget(hostname_lbl);
        hostname_layout->addWidget(hostname_input);
        hostname_layout->setContentsMargins(QMargins(0, 0, 0, 15));

        auto cookie_layout = new QVBoxLayout;
        auto cookie_lbl = new QLabel("Cookie file:");
        auto cookie_input = new QLineEdit;
        cookie_input->setText(QString::fromUtf8(g_config.cookie_file.c_str()));
        cookie_layout->addWidget(cookie_lbl);
        cookie_layout->addWidget(cookie_input);
        cookie_layout->setContentsMargins(QMargins(0, 0, 0, 15));

        auto save_button = new QPushButton("Save");
        QObject::connect(save_button, &QPushButton::released, [=]() {
            g_config.host_name = hostname_input->text().toStdString();
            g_config.cookie_file = cookie_input->text().toStdString();
        });

        QVBoxLayout *option_layout = new QVBoxLayout;
        option_layout->addLayout(hostname_layout);
        option_layout->addLayout(cookie_layout);
        option_layout->addWidget(save_button);
        option_layout->addStretch(1);

        option_page->setLayout(option_layout);

        return option_page;
    }
} // namespace interface
