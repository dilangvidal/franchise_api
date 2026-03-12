# Ambos archivos de test
./mvnw test -Dtest="FranchiseUseCaseImplTest,FranchiseControllerTest"

# Solo lógica de negocio
./mvnw test -Dtest="FranchiseUseCaseImplTest"

# Solo controlador
./mvnw test -Dtest="FranchiseControllerTest"

# Todos los tests del proyecto
./mvnw test
