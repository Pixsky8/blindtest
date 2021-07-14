#include "interface/answer.hh"

#include <QPushButton>
#include <QTextEdit>
#include <QVBoxLayout>
#include <QWidget>

#include "config.hh"
#include "network/request.hh"

namespace interface {
    AnswerWidget::AnswerWidget() {
        auto answers_label = new QTextEdit("");
        answers_label->setReadOnly(true);

        auto update_button = new QPushButton("Update");
        QObject::connect(update_button, &QPushButton::released, [=]() {
            auto request = network::answer_request(g_config.host_name,
                                                   g_config.cookie_file);
            this->leaderboard = request.perform();
            answers_label->setText(this->leaderboard.c_str());
        });

        QVBoxLayout *answers_layout = new QVBoxLayout;
        answers_layout->addWidget(answers_label);
        answers_layout->addWidget(update_button);

        this->setLayout(answers_layout);
    }
} // namespace interface
