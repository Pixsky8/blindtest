#pragma once

#include <QWidget>
#include <string>

namespace interface {
    class AnswerWidget : public QWidget {
    public:
        AnswerWidget();

        std::string leaderboard;
    };
} // namespace interface
