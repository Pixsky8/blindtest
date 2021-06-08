#include <QLabel>
#include <QPushButton>
#include <QVBoxLayout>
#include <QWidget>

#include "config.hh"
#include "network/request.hh"

namespace interface {
    QWidget *create_answers_page() {
        auto answers_page = new QWidget;

        auto answers_label = new QLabel("");

        auto update_button = new QPushButton("Update");
        QObject::connect(update_button, &QPushButton::released, [=]() {
            auto request = network::answer_request(g_config.host_name,
                                                   g_config.cookie_file);
            answers_label->setText(request.perform().c_str());
        });

        QVBoxLayout *answers_layout = new QVBoxLayout;
        answers_layout->addWidget(answers_label);
        answers_layout->addWidget(update_button);

        answers_page->setLayout(answers_layout);

        return answers_page;
    }
} // namespace interface
