#pragma once

#include <QLCDNumber>
#include <QWidget>

namespace interface {
    class QuestionWidget : public QWidget {
    private:
        int question_id = -1;
        QLCDNumber *question_number_lbl;

    public:
        QuestionWidget();
        void update_question_id();
        bool change_question_request();
    };
} // namespace interface
