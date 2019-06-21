



ARG BUILD_NL_VERSION=VERSION_TO_REPLACE
FROM  neotys/neoload-loadgenerator:$BUILD_NL_VERSION

WORKDIR /neoload

RUN ls -l

ENV user=neoload
ENV group=0
VOLUME /etc/secrets

ADD https://storage.cloud.google.com/genesis-public/cli/qa/bin/linux/amd64/whiteblock wb

RUN chown neoload:0 /neoload/wb

RUN chmod 755 /neoload/wb
RUN export PATH=$PATH:/neoload/wb


