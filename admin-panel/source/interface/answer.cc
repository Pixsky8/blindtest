#include "interface/answer.hh"

#include <QPushButton>
#include <QTextEdit>
#include <QVBoxLayout>
#include <QWidget>

#include "config.hh"
#include "json.hpp"
#include "network/request.hh"

using nlohmann::json;

namespace interface {
    AnswerWidget::AnswerWidget() {
        auto answers_label = new QTextEdit("");
        answers_label->setReadOnly(true);
        answers_label->setSizePolicy(QSizePolicy::Expanding,
                                     QSizePolicy::Expanding);

        auto update_button = new QPushButton("Update");
        QObject::connect(update_button, &QPushButton::released, [=]() {
            auto request = network::answer_request(g_config.host_name,
                                                   g_config.cookie_file);
            this->leaderboard = request.perform();
            std::string pretty = json::parse(this->leaderboard).dump(2);
            answers_label->setText(pretty.c_str());
        });

        QVBoxLayout *answers_layout = new QVBoxLayout;
        answers_layout->addWidget(answers_label);
        answers_layout->addWidget(update_button);

        this->setLayout(answers_layout);
    }
} // namespace interface
