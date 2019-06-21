FROM anapsix/alpine-java as builder



ARG BUILD_NL_VERSION=VERSION_TO_REPLACE

ADD https://www.neotys.com/redirect/redirect.php?product=neoload&target=direct-download&os=linux&archi=x64&version=$BUILD_NL_VERSION neoload.sh

RUN ls -l
RUN sh /neoload.sh -q -dir /neoload -Vsys.installationTypeId=Load Generator -Vsys.component.Common\$Boolean=true -Vsys.component.Load\ Generator$Boolean=true -Vsys.component.Monitor\ Agent$Boolean=false -Vsys.languageId=en -Vsys.programGroupDisabled$Boolean=true -Vsys.executeLauncherAction$Boolean=false -Vsys.installService$Boolean=true -Vsys.startOnBootUp$Boolean=true -Vsys.adminRights$Boolean=false


FROM alpine:3.7

WORKDIR /neoload

RUN ls -l

ENV user=root
ENV group=root
VOLUME /etc/secrets

ADD https://storage.cloud.google.com/genesis-public/cli/qa/bin/linux/amd64/whiteblock wb


RUN chmod 755 /neoload/wb
RUN export PATH=$PATH:/neoload/wb

EXPOSE 7100


ENTRYPOINT ["/neoload/bin/LoadGeneratorAgent -d"]