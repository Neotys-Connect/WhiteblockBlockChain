

ARG BUILD_NL_VERSION=VERSION_TO_REPLACE

FROM  neotys/neoload-loadgenerator:$BUILD_NL_VERSION

WORKDIR /neoload

RUN ls -l

ENV user=neoload
ENV group=0
VOLUME /etc/secrets
USER root

ADD https://storage.cloud.google.com/genesis-public/cli/qa/bin/linux/amd64/whiteblock wb

RUN ls -l /neoload
RUN chown neoload /neoload/wb
RUN chmod 755 /neoload/wb


USER neoload

ENV  PATH="$PATH:/neoload/wb"
