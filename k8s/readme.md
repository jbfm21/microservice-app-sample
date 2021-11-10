## Install keycloack in kubernete (https://medium.com/@jinnerbichler/securing-spring-boot-applications-with-keycloak-on-kubernetes-76cdb6b8d674)

### Set up database
```
The following command creates the necessary database with proper configurations (e.g. pods and services):

helm repo add bitnami https://charts.bitnami.com/bitnami
helm search repo postgresql

helm install keycloak-db --set postgresUser=admin --set postgresPassword=password --set postgresDatabase=keycloak-db  bitnami/postgresql

The name of the corresponding service is keycloak-db, which runs the chart stable/postgresql. The name of the started pod can be obtained by executing:
echo $(kubectl get pods --namespace default -l "app=postgresql,release=keycloak-db" -o jsonpath="{.items[0].metadata.name}")	

C:\dsv\helm>helm install keycloak-db --set postgresUser=admin --set postgresPassword=password --set postgresDatabase=keycloak-db  bitnami/postgresql
NAME: keycloak-db
LAST DEPLOYED: Fri Oct 22 23:41:31 2021
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
CHART NAME: postgresql
CHART VERSION: 10.12.7
APP VERSION: 11.13.0

** Please be patient while the chart is being deployed **

PostgreSQL can be accessed via port 5432 on the following DNS names from within your cluster:

    keycloak-db-postgresql.default.svc.cluster.local - Read/Write connection

To get the password for "postgres" run:

    export POSTGRES_PASSWORD=$(kubectl get secret --namespace default keycloak-db-postgresql -o jsonpath="{.data.postgresql-password}" | base64 --decode)

To connect to your database run the following command:

    kubectl run keycloak-db-postgresql-client --rm --tty -i --restart='Never' --namespace default --image docker.io/bitnami/postgresql:11.13.0-debian-10-r67 --env="PGPASSWORD=$POSTGRES_PASSWORD" --command -- psql --host keycloak-db-postgresql -U postgres -d postgres -p 5432



To connect to your database from outside the cluster execute the following commands:

    kubectl port-forward --namespace default svc/keycloak-db-postgresql 5432:5432 &
    PGPASSWORD="$POSTGRES_PASSWORD" psql --host 127.0.0.1 -U postgres -d postgres -p 5432

```

### Intialize kubermete

kubectl apply -f keycloak-deployment.yml

### See log output
```
POD_NAME=$(kubectl get pods -l name=keycloak -o go-template --template '{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}') kubectl logs -f ${POD_NAME}
```

### Access keycloack

- After Keycloak started successfully the respective URL can be displayed via: minikube service keycloak --url
