FROM maven:3.6.3-openjdk-11

ADD . /app
WORKDIR /app

VOLUME [ "/config" ]

RUN bash /app/compile.sh
RUN mvn dependency:resolve-plugins
RUN mvn dependency:go-offline

RUN /app/run.sh --help # exec server to download the rest of the deps

CMD bash /app/run.sh /config
