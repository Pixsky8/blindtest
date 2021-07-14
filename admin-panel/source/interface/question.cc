#include "interface/question.hh"

#include <QLCDNumber>
#include <QLabel>
#include <QMessageBox>
#include <QPushButton>
#include <QSpinBox>
#include <QVBoxLayout>

#include "json.hpp"

#include "config.hh"
#include "network/request.hh"

using nlohmann::json;

namespace interface {
    bool QuestionWidget::change_question_request() {
        auto request = network::question_request(g_config.host_name,
                                                 g_config.cookie_file,
                                                 this->question_id);

        auto res = request.perform();

        QMessageBox response;
        response.setText(QString::fromUtf8(res.c_str()));
        response.exec();

        if (res.empty())
            return false;

        try {
            json res_json = json::parse(res);

            auto succ = res_json.find("status");
            if (succ == res_json.end())
                return false;

            return *succ == "SUCCESS";
        }
        catch (const std::exception &e) {
            return false;
        }
    }

    QuestionWidget::QuestionWidget() {
        auto layout = new QVBoxLayout;

        question_number_lbl = new QLCDNumber;
        // TODO: get current question from the server
        question_number_lbl->display(this->question_id);
        question_number_lbl->setMinimumHeight(100);
        layout->addWidget(question_number_lbl);

        auto prev_button = new QPushButton("Previous Question");
        QObject::connect(prev_button, &QPushButton::released, [=]() {
            this->question_id--;
            if (!this->change_question_request())
                this->question_id++;
            this->update_question_id();
        });

        auto next_button = new QPushButton("Next Question");
        QObject::connect(next_button, &QPushButton::released, [=]() {
            this->question_id++;
            if (!this->change_question_request())
                this->question_id--;
            this->update_question_id();
        });

        auto buttons_layout = new QHBoxLayout;
        buttons_layout->addWidget(prev_button);
        buttons_layout->addWidget(next_button);
        layout->addLayout(buttons_layout);

        auto setter_lbl = new QLabel("Set Question Number:");
        layout->addWidget(setter_lbl);

        auto spinner = new QSpinBox;
        spinner->setMaximum(INT32_MAX);
        layout->addWidget(spinner);

        auto update_button = new QPushButton("Set Question");
        QObject::connect(update_button, &QPushButton::released, [=]() {
            int old = this->question_id;
            this->question_id = spinner->value();
            if (!this->change_question_request())
                this->question_id = old;
            this->update_question_id();
        });
        layout->addWidget(update_button);

        layout->addStretch(1);
        this->setLayout(layout);
    }

    void QuestionWidget::update_question_id() {
        question_number_lbl->display(this->question_id);
    }
} // namespace interface
