#include "interface/question.hh"

#include <QLCDNumber>
#include <QLabel>
#include <QMessageBox>
#include <QPushButton>
#include <QSpinBox>
#include <QVBoxLayout>

#include "config.hh"
#include "network/request.hh"

namespace interface {
    void QuestionWidget::change_question_request() {
        auto request = network::question_request(g_config.host_name,
                                                 g_config.cookie_file,
                                                 this->question_id);

        QMessageBox response;
        response.setText(QString::fromUtf8(request.perform().c_str()));
        response.exec();
    }

    QuestionWidget::QuestionWidget() {
        auto layout = new QVBoxLayout;

        question_number_lbl = new QLCDNumber;
        question_number_lbl->display(this->question_id);
        question_number_lbl->setMinimumHeight(100);
        layout->addWidget(question_number_lbl);

        auto buttons_layout = new QHBoxLayout;
        auto prev_button = new QPushButton("Previous Question");
        // TODO: add listener
        buttons_layout->addWidget(prev_button);
        auto next_button = new QPushButton("Next Question");
        // TODO: add listener
        buttons_layout->addWidget(next_button);
        layout->addLayout(buttons_layout);

        auto setter_lbl = new QLabel("Set Question Number:");
        layout->addWidget(setter_lbl);

        auto spinner = new QSpinBox;
        spinner->setMaximum(INT32_MAX);
        layout->addWidget(spinner);

        auto update_button = new QPushButton("Set Question");
        QObject::connect(update_button, &QPushButton::released, [=]() {
            this->question_id = spinner->value();
            this->change_question_request();
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
