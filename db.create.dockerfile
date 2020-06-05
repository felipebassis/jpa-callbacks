FROM postgres:12.1
RUN localedef -i pt_BR -c -f UTF-8 -A /usr/share/locale/locale.alias pt_BR.UTF-8
ENV POSTGRES_DB demo-jpa-callbacks
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD root
ENV LANG pt_BR.UTF8
ENV TZ America/Sao_Paulo