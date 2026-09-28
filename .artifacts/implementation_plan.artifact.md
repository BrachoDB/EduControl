# Plan de Implementación - Fases 4 y 5: Pantalla Principal y Detalle de Materias

Este plan cubre el desarrollo de la `HomeActivity` (Fase 4), donde se listarán las materias del usuario y su promedio general, y el flujo de registro y detalle de notas (Fase 5).

## User Review Required

> [!IMPORTANT]
> Estas fases involucran el paso de datos entre Activities usando `Intent` (específicamente el `userId` para saber de quién son las materias, y el `materiaId` para las notas).
> Modificaremos el `LoginActivity` para que envíe el ID del usuario al `HomeActivity` al iniciar sesión con éxito.

## Proposed Changes

### [Componente] Fase 4: Home y Listado de Materias

#### [NEW] [activity_home.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/activity_home.xml)
- `TextView` destacado para el Promedio General.
- `RecyclerView` para la lista de materias.
- `FloatingActionButton` (FAB) para agregar una nueva materia.

#### [NEW] [item_materia.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/item_materia.xml)
Diseño de la tarjeta (`MaterialCardView`) para cada materia, mostrando nombre, profesor y promedio de la materia.

#### [NEW] [MateriaAdapter.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/home/MateriaAdapter.kt)
Adaptador del RecyclerView para gestionar la lista de `MateriaEntity`. Incluirá un listener para los clics en cada tarjeta.

#### [NEW] [HomeActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/home/HomeActivity.kt)
Lógica para recuperar el `userId` del intent, cargar las materias desde Room y calcular el promedio general.

---

### [Componente] Fase 5: Formularios y Detalle de Evaluaciones

#### [NEW] [activity_add_materia.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/activity_add_materia.xml) y [AddMateriaActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/materia/AddMateriaActivity.kt)
Formulario para crear una nueva materia vinculada al usuario actual.

#### [NEW] [activity_detalle_materia.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/activity_detalle_materia.xml) y [DetalleMateriaActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/materia/DetalleMateriaActivity.kt)
Pantalla que muestra los detalles de una materia seleccionada, incluyendo un `RecyclerView` con sus notas específicas y un FAB para agregar notas.

#### [NEW] [item_nota.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/item_nota.xml) y [NotaAdapter.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/materia/NotaAdapter.kt)
Diseño y adaptador para listar las evaluaciones (ej. "Parcial 1 - 4.5 [30%]").

#### [NEW] [activity_add_nota.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/activity_add_nota.xml) y [AddNotaActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/materia/AddNotaActivity.kt)
Formulario para registrar una evaluación. Incluirá validación para asegurar que la suma de porcentajes de las notas de la materia no exceda el 100%.

#### [MODIFY] [LoginActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/auth/LoginActivity.kt)
Reemplazar el `// TODO` por el intent de navegación hacia `HomeActivity`, pasando el `user.id`.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/AndroidManifest.xml)
Registrar las 4 nuevas actividades.

## Verification Plan

### Manual Verification
1. Loguearse con un usuario. Verificar transición al Home.
2. Crear una materia y verificar que aparezca en el RecyclerView.
3. Entrar al detalle de la materia y agregar 2 notas.
4. Validar que no se pueda exceder el 100% en las notas.
5. Volver al Home y validar que el promedio general se haya recalculado.
