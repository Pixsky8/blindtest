#include "interface/score.hh"

#include <QLineEdit>
#include <QMessageBox>
#include <QPushButton>
#include <QSpinBox>
#include <QVBoxLayout>

#include "json.hpp"

#include "config.hh"
#include "network/request.hh"

using nlohmann::json;

namespace interface {
    static bool send_point_update(const std::string &username, int points) {
        auto request = network::give_points_request(g_config.host_name,
                                                    g_config.cookie_file,
                                                    username,
                                                    points);

        QMessageBox response;

        std::string error_str;

        auto body = request.perform();
        try {
            json body_json = json::parse(body);
            auto status = body_json.find("status");
            if (status != body_json.end() && *status == "SUCCESS") {
                response.setText(QString::fromUtf8((body).c_str()));
                response.exec();
                return true;
            }
        }
        catch (const nlohmann::detail::parse_error &e) {
            error_str = e.what();
        }
        catch (const std::exception &e) {
            error_str = e.what();
        }

        response.setText(QString::fromUtf8((body + "\n" + error_str).c_str()));
        response.exec();
        return false;
    }

    ScoreWidget::ScoreWidget() {
        auto layout = new QVBoxLayout;

        auto username_input = new QLineEdit;
        auto spinner = new QSpinBox;
        auto accept_button = new QPushButton("Update");

        layout->addWidget(username_input);
        layout->addWidget(spinner);
        layout->addWidget(accept_button);

        QObject::connect(accept_button, &QPushButton::released, [=]() {
            send_point_update(username_input->text().toStdString(),
                              spinner->value());
        });

        this->setLayout(layout);
    }
} // namespace interface
