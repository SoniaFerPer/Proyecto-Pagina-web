CREATE DATABASE IF NOT EXISTS dnd_app
CHARACTER SET utf8mb4
COLLATE utf8mb4_spanish_ci;
USE dnd_app;

-- =====================================================
-- TABLA RAZAS
-- =====================================================
CREATE TABLE razas (
    id_raza INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    imagen VARCHAR(255)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- TABLA CLASES
-- =====================================================
CREATE TABLE clases (
    id_clase INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    imagen VARCHAR(255)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- ATRIBUTOS
-- =====================================================
CREATE TABLE atributos (
    id_atributo INT AUTO_INCREMENT PRIMARY KEY,
    
    nombre VARCHAR(30) NOT NULL UNIQUE,
    
    descripcion TEXT NOT NULL

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- BONIFICACIONES RACIALES
-- =====================================================
CREATE TABLE raza_bonificaciones (
    id_bonus_raza INT AUTO_INCREMENT PRIMARY KEY,
    id_raza INT NOT NULL,
    id_atributo INT NOT NULL,
    valor INT NOT NULL,
    FOREIGN KEY (id_raza)
        REFERENCES razas(id_raza)
        ON DELETE CASCADE,
        FOREIGN KEY (id_atributo)
        REFERENCES atributos(id_atributo)
        ON DELETE CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- BONIFICACIONES DE CLASE
-- =====================================================
CREATE TABLE clase_bonificaciones (
    id_bonus_clase INT AUTO_INCREMENT PRIMARY KEY,
    id_clase INT NOT NULL,
    id_atributo INT NOT NULL,
    valor INT NOT NULL,
    FOREIGN KEY (id_clase)
        REFERENCES clases(id_clase)
        ON DELETE CASCADE,
        FOREIGN KEY (id_atributo)
        REFERENCES atributos(id_atributo)
        ON DELETE CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- TABLA COMBINACIONES
-- =====================================================
CREATE TABLE combinaciones (
    id_combinacion INT AUTO_INCREMENT PRIMARY KEY,
    id_raza INT NOT NULL,
    id_clase INT NOT NULL,
    titulo VARCHAR(100),
    nombre_completo VARCHAR(100),
    descripcion TEXT,
    imagen VARCHAR(255),
    FOREIGN KEY (id_raza)
        REFERENCES razas(id_raza)
        ON DELETE CASCADE,
    FOREIGN KEY (id_clase)
        REFERENCES clases(id_clase)
        ON DELETE CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- TABLA PERSONAJES
-- =====================================================
CREATE TABLE personajes (

    id_personaje INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,

    id_raza INT NOT NULL,
    id_clase INT NOT NULL,

    nivel INT DEFAULT 1,
    experiencia INT DEFAULT 0,

    vida_actual INT DEFAULT 100,
    mana_actual INT DEFAULT 50,

    oro INT DEFAULT 0,

    inventario TEXT,
    habilidades TEXT,

    FOREIGN KEY (id_raza)
        REFERENCES razas(id_raza)
        ON DELETE RESTRICT,

    FOREIGN KEY (id_clase)
        REFERENCES clases(id_clase)
        ON DELETE RESTRICT

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- TABLA HABILIDADES 
-- =====================================================
DROP TABLE IF EXISTS habilidades;
CREATE TABLE habilidades (
    id_habilidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT,
    estadistica_base VARCHAR(20) NOT NULL,
    requisito_minimo INT NOT NULL
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- ATRIBUTOS DE PERSONAJES
-- =====================================================

CREATE TABLE personaje_atributos (

    id_personaje INT NOT NULL,
    id_atributo INT NOT NULL,

    valor INT DEFAULT 0,

    PRIMARY KEY (id_personaje, id_atributo),

    FOREIGN KEY (id_personaje)
        REFERENCES personajes(id_personaje)
        ON DELETE CASCADE,

    FOREIGN KEY (id_atributo)
        REFERENCES atributos(id_atributo)
        ON DELETE CASCADE

) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- TABLA INVENTARIO 
-- =====================================================
CREATE TABLE inventario (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo ENUM('ARMA', 'ARMADURA', 'POCION', 'OBJETO_MAGICO', 'HERRAMIENTA', 'MISCELANEO') NOT NULL,
    subtipo VARCHAR(50),
    precio VARCHAR(50),
    peso VARCHAR(30),
    dano VARCHAR(20),
    propiedades TEXT,
    descripcion TEXT,
    rareza VARCHAR(30) DEFAULT 'Común'
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_spanish_ci;

-- =====================================================
-- INSERT RAZAS
-- =====================================================

INSERT INTO razas (id_raza, nombre, descripcion) VALUES
(1,'Humano','Los humanos son la raza más adaptable y numerosa del continente.'),
(2,'Enano','Los enanos son resistentes guerreros de las montañas y fortalezas subterráneas.'),
(3,'Mediano','Los medianos son criaturas ágiles, discretas y muy sociables.'),
(4,'Elfo','Los elfos son seres antiguos ligados a la magia y la naturaleza.'),
(5,'Draconido','Los dracónidos descienden de antiguos linajes de dragones.'),
(6,'Tiefling','Los tieflings poseen sangre infernal y una presencia inquietante.');

-- =====================================================
-- INSERT ATRIBUTOS
-- =====================================================

INSERT INTO atributos (nombre, descripcion) VALUES

('FUERZA','Representa el poder físico del personaje. Influye en el daño cuerpo a cuerpo, la capacidad de carga, los saltos y las pruebas relacionadas con esfuerzo físico.'),

('DESTREZA','Representa la agilidad, reflejos y coordinación. Afecta la armadura, iniciativa, ataques a distancia, sigilo y acrobacias.'),

('CONSTITUCIÓN','Representa la resistencia física y salud del personaje. Determina los puntos de vida máximos, resistencia al veneno y capacidad de soportar fatiga.'),

('INTELIGENCIA','Representa el conocimiento, lógica y capacidad analítica. Determina el maná máximo del personaje y potencia habilidades mágicas arcanas, investigación y razonamiento.'),

('SABIDURÍA','Representa la percepción, intuición y conexión espiritual. Afecta percepción, supervivencia, medicina y magia divina.'),

('CARISMA','Representa la personalidad, liderazgo y capacidad social. Influye en persuasión, intimidación, engaño y ciertas magias.');

-- =====================================================
-- BONIFICACIONES RACIALES
-- =====================================================

INSERT INTO raza_bonificaciones
(id_raza, id_atributo, valor)
VALUES

-- Humanos
(1,1,1),
(1,2,1),
(1,3,1),
(1,4,1),
(1,5,1),
(1,6,1),

-- Enanos
(2,3,2),
(2,1,1),

-- Medianos
(3,2,2),
(3,6,1),

-- Elfo (id_raza = 4)
(4,2,2), -- DESTREZA
(4,4,1), -- INTELIGENCIA
(4,5,1), -- SABIDURIA

-- Draconido (id_raza = 5)
(5,1,2), -- FUERZA
(5,3,2), -- CONSTITUCION
(5,6,1), -- CARISMA (presencia dracónica)

-- Tiefling (id_raza = 6)
(6,6,2), -- CARISMA
(6,4,1), -- INTELIGENCIA
(6,5,1); -- SABIDURIA (voluntad/resistencia mental)

-- =====================================================
-- INSERT CLASES
-- =====================================================

INSERT INTO clases (id_clase, nombre, descripcion) VALUES
(1,'Guerrero','Especialistas en combate físico y armaduras pesadas.'),
(2,'Pícaro','Expertos en sigilo, robo y ataques rápidos.'),
(3,'Mago','Eruditos del poder arcano y la magia.'),
(4,'Clérigo','Canalizadores de poder divino y sanación.'),
(5,'Druida','Guardianes de la naturaleza y las fuerzas salvajes.'),
(6,'Explorador','Rastreadores expertos y supervivientes natos.');

-- =====================================================
-- BONIFICACIONES DE CLASE
-- =====================================================

INSERT INTO clase_bonificaciones (id_clase, id_atributo, valor) VALUES
(1, 1, 2), -- FUERZA
(2, 2, 2), -- DESTREZA
(3, 4, 2), -- INTELIGENCIA
(4, 5, 2), -- SABIDURIA
(5, 5, 1),
(5, 2, 1),
(6, 2, 1),
(6, 4, 1);

-- =====================================================
-- COMBINACIONES
-- =====================================================

INSERT INTO combinaciones(id_raza, id_clase, titulo, nombre_completo, descripcion, imagen) VALUES

(1,1,'El León de Valdoria','Humano Guerrero','Procedente de los reinos fronterizos de Valdoria, este humano posee una constitución equilibrada y adaptable, acostumbrada tanto a la guerra como a la supervivencia diaria. convirtiéndolo en un combatiente sólido y versátil, porta una espada larga desgastada por innumerables batallas y una armadura pesada marcada por cicatrices de combate. Su mirada refleja experiencia, disciplina y la determinación de alguien que jamás abandona a sus compañeros.','/img/1_1.png'),

(1,2,'Mano Sombria de Noctis','Humano Picaro','Originario de los callejones y mercados oscuros de Noctis, este humano posee una constitución ligera y flexible, perfecta para desaparecer entre las sombras. Su sonrisa confiada y su mirada astuta delatan a alguien acostumbrado al engaño y al peligro. Viste ropas oscuras y discretas, llenas de bolsillos ocultos para guardar dagas, monedas robadas y herramientas de ladrón.','/img/1_2.png'),

(1,3,'El Sabio de Elarion','Humano Mago','Criado en las academias arcanas de Elarion, este humano ha dedicado su vida al estudio de los secretos del universo.  Sus túnicas están cubiertas de símbolos arcanos y siempre transporta grimorios antiguos sujetos por cadenas de plata. Habla con calma y precisión, como alguien acostumbrado a pensar antes de actuar.','/img/1_3.png'),

(1,4,'Santo de Valdoria','Humano Clerigo','Originario de los grandes templos de Valdoria, este humano posee una constitución equilibrada y resistente, forjada por años de peregrinaciones y disciplina espiritual, don propio de su profunda conexión divina. Su piel está marcada por el sol de los caminos y sus ojos transmiten una fe inquebrantable. Viste túnicas ceremoniales bajo una cota ligera y porta un símbolo sagrado grabado en plata. Su presencia inspira calma y autoridad, como si los dioses caminaran a su lado.','/img/1_4.png'),

(1,5,'Lobo de Sylvaran','Humano Druida','Proveniente de los bosques eternos de Sylvaran, este humano posee una constitución ágil y resistente, moldeada por la vida entre árboles y criaturas salvajes, combinación ideal para canalizar la magia natural y moverse con sigilo por la espesura. Su cabello suele estar enredado con hojas, plumas y pequeñas ramas, mientras que sus ropas están hechas de pieles y fibras naturales. Los animales se sienten cómodos cerca de él, y su sola presencia transmite la serenidad y ferocidad de la naturaleza.','/img/1_5.png'),

(1,6,'Trancos de Karak Veld','Humano Explorador','Criado en las fronteras montañosas de Karak Veld, este humano tiene un cuerpo atlético y endurecido por el clima extremo y las largas travesías.Sus ropas están diseñadas para sobrevivir a cualquier entorno, y siempre lleva un arco largo, cuchillos de caza y herramientas de supervivencia. Sus ojos permanecen alerta en todo momento, acostumbrados a detectar peligros antes que nadie.','/img/1_6.png'),

(2,1,'La Muralla de Khaz-Dûram','Enano Guerrero','Forjado en las profundidades de Khaz-Dûram, este enano posee una constitución casi inquebrantable, moldeada por la guerra subterránea y la minería eterna. Cubierto de cicatrices y con una armadura de placas macizas, empuña un hacha de guerra que parece demasiado grande para cualquier otra criatura. Su carácter es directo, terco y absolutamente indomable en combate.','/img/2_1.png'),

(2,2,'Sombra mano de Mithil','Enano Picaro','Desde los bajos fondos de Noctis, este enano rompe todas las expectativas de su raza. Su resistencia y agilidad lo hacen un ladrón casi imposible de atrapar. Se mueve entre sombras con una precisión quirúrgica, desapareciendo entre callejones como humo.','/img/2_2.png'),

(2,3,'El sabio de Khaz-Dûram','Enano Mago','Desde las bibliotecas subterráneas de Khaz-Dûram, este enano desafía los estereotipos de su raza. Su constitución resistente le permite soportar estudios arcanos peligrosos, mientras inteligencia lo convierte en un erudito implacable. Sus runas brillan en su piel como hierro encantado.','/img/2_3.png'),

(2,4,'La Voz de la Montaña','Enano Clerigo','Procedente de los templos pétreos de Barak-Thul, este enano combina fe y resistencia absoluta. Porta un martillo sagrado grabado con runas ancestrales y una armadura ceremonial reforzada con oro y acero negro. Cuando pronuncia plegarias, su voz retumba como un eco dentro de una montaña.','/img/2_4.png'),

(2,5,'La raiz de Piedra','Enano Druida','Criado en cavernas vivas bajo raíces antiguas de Sylvaran, este enano combina roca y naturaleza. Su resistencia natural se une a su conexión espiritual y agilidad terrestre. Su barba está llena de musgo y minerales vivos, como si la tierra lo hubiera reclamado.','/img/2_5.png'),

(2,6,'Martillo de Karak Veld','Enano Explorador','Forjado en los túneles fronterizos de Karak Veld, este enano es un rastreador incansable. Su dureza física y su entrenamiento lo convierten en un explorador perfecto de cavernas y montañas. Sus ojos detectan rutas donde otros solo ven piedra.','/img/2_6.png'),

(3,1,'Lanza de Cuatro colinas','Mediano Guerrero','Criado en las colinas fértiles de Las Cuatro Colinas, este mediano demuestra que la fuerza no depende del tamaño. Su fisiología ligera y su entrenamiento marcial lo convierten en un combatiente sorprendentemente peligroso. Aunque su apariencia es amable y poco intimidante, en combate se mueve con una agresividad rápida y precisa. Usa armadura ligera adaptada a su cuerpo pequeño y armas proporcionales, pero perfectamente equilibradas. Es un guerrero que confía en la velocidad, la sorpresa y la precisión más que en la fuerza bruta.','/img/3_1.png'),

(3,2,'La Sombra de Verdearroyo','Mediano Picaro','Crecido entre mercados, tabernas y callejones de Verdearroyo, este mediano es un maestro absoluto del sigilo y la improvisación. Su destreza racial, junto con su entrenamiento como pícaro, lo convierten en un especialista natural en infiltración y escape. Siempre lleva ropa cómoda llena de bolsillos ocultos y pequeñas herramientas de cerrajería. Su personalidad relajada y amistosa oculta una mente increíblemente rápida y peligrosa.','/img/3_2.png'),

(3,3,'Lince de Verdearroyo','Mediano Mago','Formado en hogares curiosos y bibliotecas improvisadas de Verdearroyo, este mediano ha desarrollado una mente brillante a través de la observación constante del mundo. Su destreza le permite evitar peligros con facilidad, mientras su inteligencia lo convierte en un estudioso arcano ingenioso y creativo. Sus grimorios son pequeños, portátiles y llenos de anotaciones caóticas. Suele improvisar con materiales cotidianos para lanzar hechizos, demostrando que la magia no siempre necesita formalidad para ser poderosa.','/img/3_3.png'),

(3,4,'Sanador de Verdearroyo','Mediano Clerigo','Originario de las comunidades tranquilas de Verdearroyo, este mediano es un guía espiritual humilde y cercano. Su destreza refleja su naturaleza ágil y viajera, mientras su sabiduría lo convierte en un sanador intuitivo y confiable. Viste ropas sencillas de viajero con símbolos religiosos discretos, más prácticos que ceremoniales. Lleva un pequeño símbolo sagrado colgado al cuello y un kit de curación siempre listo. Su presencia transmite calma, cercanía y una fe sencilla pero sólida, capaz de unir comunidades enteras.','/img/3_4.png'),

(3,5,'Caminante Cuatro Colinas','Mediano Druida','Crecido entre senderos rurales y colinas vivas de Las Cuatro Colinas, este mediano vive en armonía absoluta con la naturaleza lo que lo convierten en un guardián resistente, perceptivo y ágil. Camina descalzo con frecuencia y conoce cada planta, animal y corriente de viento de su hogar. Su magia druídica es suave pero constante, como una conversación permanente con la tierra.','/img/3_5.png'),

(3,6,'El Caminante de las Colinas','Mediano Explorador','Procedente de las rutas rurales de Las Cuatro Colinas, este mediano es un viajero incansable acostumbrado a sobrevivir lejos de cualquier ciudad. Porta un arco corto perfectamente equilibrado y un conjunto de herramientas de supervivencia cuidadosamente organizado. Sus pasos son ligeros y silenciosos, como si el propio camino quisiera ocultarlo.','/img/3_6.png'),

(4,1,'La Espada de Lunareth','Elfo Guerrero','Entrenado en los salones de guerra de Lunareth, este elfo representa la unión perfecta entre gracia y letalidad. Su destreza racial le otorga movimientos elegantes y precisos con una fuerza descomunal. Porta una espada larga élfica forjada en plata azulada y una armadura ligera encantada que apenas produce sonido. En combate se mueve como una danza perfectamente calculada.','/img/4_1.png'),

(4,2,'El Fantasma del Bosque Eterno','Elfo Picaro','Crecido entre sombras y senderos ocultos de Lunareth, este elfo ha perfeccionado el arte del sigilo hasta niveles casi sobrenaturales, convirtiéndolo en una sombra imposible de rastrear. Viste cuero oscuro encantado y porta dagas finas de precisión quirúrgica. Nunca permanece demasiado tiempo en el mismo lugar.','/img/4_2.png'),

(4,3,'Astro de Lunareth','Elfo Mago','Proveniente de los eternos bosques de Lunareth, este elfo posee una constitución elegante y esbelta, marcada por siglos de estudio y perfección. Gracias a su dominio de las artes arcanas, sus ojos brillan con energía mágica y sus movimientos parecen casi irreales. Porta túnicas bordadas con símbolos antiguos y un bastón tallado en madera élfica encantada.','/img/4_3.png'),

(4,4,'Sacerdote de Sylvandor','Elfo Clerigo','Procedente de los santuarios vivos de Sylvandor, este elfo canaliza la fe como una extensión del propio mundo natural, convirtiéndolo en un intermediario entre lo divino y lo natural. Sus ropas están hechas de tejidos vivos entrelazados con hojas y símbolos sagrados, que cambian lentamente con las estaciones. Porta un bastón de madera viva que responde a su estado emocional. Su presencia transmite calma absoluta, como si el bosque mismo escuchara sus plegarias.','/img/4_4.png'),

(4,5,'Espiritu Salvaje de Sylvandor','Elfo Druida','Nacido en los santuarios naturales de Sylvandor, este elfo vive en perfecta armonía con la naturaleza, representando su conexión espiritual y resistencia salvaje. Sus ropas están hechas de fibras vivas y adornadas con hojas y flores. Los animales lo siguen sin temor y el viento parece responder a sus palabras.','/img/4_5.png'),

(4,6,'Huella del Bosque Sombrio','Elfo Explorador','Entrenado en los límites del reino de Sylvandor, este elfo es un rastreador perfecto. Diseñado para la vigilancia y la caza precisa, lo convierten en un explorador altamente eficiente y casi invisible. Sus pasos no dejan huella y su respiración se sincroniza con el entorno natural. Porta un arco largo élfico perfectamente equilibrado y flechas preparadas para cualquier tipo de terreno o criatura.','/img/4_6.png'),

(5,1,'El Coloso de Varkesh','Draconido Guerrero','Forjado en las arenas de guerra del imperio de Varkesh, este dracónido es la encarnación de la fuerza bruta y la disciplina militar. Su linaje dracónico lo convierte en una fuerza de destrucción imparable con un cuerpo está cubierto de escamas endurecidas por el combate, con cicatrices que brillan ligeramente como si aún ardieran. Porta una armadura pesada reforzada con placas metálicas negras y un arma diseñada para romper líneas enemigas. Su presencia en el campo de batalla no es solo intimidante: es devastadora.','/img/5_1.png'),

(5,2,'Sombra diurna de Arkhosia','Draconido Picaro','Crecido en las sombras políticas y callejones secretos de Arkhosia, este dracónido es una anomalía entre su propia especie, creando un depredador silencioso y extremadamente peligroso. A diferencia de otros dracónidos, sus movimientos son calculados, suaves y casi silenciosos, como una serpiente entre rocas. Su armadura ligera está diseñada para no reflejar luz, y sus armas están impregnadas con venenos o magia sutil. No ruge en combate: desaparece, golpea y ya es demasiado tarde para reaccionar.','/img/5_2.png'),

(5,3,'El Heraldo Escarlata','Draconido Mago','Originario de las cámaras arcanas ocultas de Varkesh, este dracónido ha transformado su herencia bestial en dominio intelectual del poder arcano. Sus escamas brillan ligeramente cuando canaliza energía mágica y su bastón parece contener fuego vivo en su interior. Habla poco, pero cada palabra parece cargada de poder.','/img/5_3.png'),

(5,4,'Llama de Arkhosia','Draconido Clerigo','Criado en los templos sagrados del imperio de Arkhosia, este dracónido combina la majestuosidad de su linaje con una fe absoluta en poderes superiores, convirtiéndolo en un emisario sagrado de enorme presencia. Sus escamas están decoradas con símbolos rituales dorados, y su armadura ceremonial parece diseñada tanto para guerra como para rituales. Su voz resuena como un trueno controlado, capaz de imponer orden o juicio sin levantar el arma.','/img/5_4.png'),

(5,5,'Dragon salvaje de Arkhosia','Draconido Druida','Nacido en las tierras primordiales donde el imperio de Arkhosia aún se mezcla con la naturaleza salvaje, este dracónido representa la unión entre destrucción y vida, creando un equilibrio entre poder físico y sensibilidad natural. Sus escamas están cubiertas de musgo, ceniza volcánica o cristal natural según el entorno, como si el mundo lo hubiera reclamado. Puede invocar tormentas, fuego o crecimiento salvaje con la misma naturalidad con la que otros respiran. Su presencia impone respeto tanto a bestias como a espíritus de la tierra.','/img/5_5.png'),

(5,6,'Cazador de Varkesh','Draconido Explorador','Forjado en las fronteras hostiles de Varkesh, este dracónido es un cazador nato de criaturas y ejércitos por igual, convirtiéndolo en un rastreador estratégico y brutal. Su cuerpo es más ligero que otros de su especie, optimizado para largas persecuciones y emboscadas. Porta un arco pesado adaptado a su fuerza dracónica y armas arrojadizas forjadas en metal negro. Sus sentidos son agudos como los de un depredador: nada escapa a su atención.','/img/5_6.png'),

(6,1,'Fuerza maldita de Umbraxis','Tiefling Guerrero','Nacido en los barrios de sombra de Umbraxis, este tiefling creció donde la violencia y el pacto oscuro son parte de la vida, convirtiéndolo en un combatiente tan intimidante como carismático. Su piel marcada por tonos rojizos y sus cuernos curvados lo hacen destacar incluso entre soldados veteranos. Porta una armadura reforzada con cadenas rituales y un arma pesada que parece haber sido forjada para él por fuerzas no humanas. Su presencia impone respeto inmediato: no es solo un guerrero, es una advertencia viviente.','/img/6_1.png'),

(6,2,'El Fantasma Infernal','Tiefling Picaro','Crecido en los callejones más oscuros de Umbraxis, este tiefling domina el engaño y la infiltración como si fueran un arte. Sus cuernos curvados y su mirada brillante generan desconfianza inmediata, algo que él utiliza constantemente a su favor. Siempre parece conocer secretos que nadie más debería escuchar.','/img/6_2.png'),

(6,3,'El Archivista Maldito','Tiefling Mago','Criado entre círculos arcanos prohibidos de Nethrak, este tiefling ha convertido su herencia infernal en una fuente de conocimiento oscuro. Sus túnicas están llenas de símbolos de contención y cadenas rituales, y sus ojos brillan como brasas encendidas cuando lanza hechizos. Su presencia resulta tan fascinante como perturbadora.','/img/6_3.png'),

(6,4,'Monje FuegoNegro','Tiefling Clerigo','Procedente de los templos ocultos bajo Umbraxis, este tiefling ha encontrado la fe en lugares donde otros solo ven corrupción, creando una mezcla inquietante entre luz divina y oscuridad ancestral. Viste túnicas largas con símbolos sagrados grabados sobre cuero ennegrecido, y porta un símbolo religioso que parece arder suavemente en presencia del pecado. Su voz es calmada, pero cargada de una autoridad sobrenatural que puede tanto consolar como condenar.','/img/6_4.png'),

(6,5,'Muerte de la Arboleda Corrompida','Tiefling Druida','Forjado en los márgenes corrompidos de Sylvaran, este tiefling representa el equilibrio entre lo natural y lo infernal, convirtiéndolo en un intermediario entre mundos que no deberían mezclarse. Sus cuernos están cubiertos de enredaderas y símbolos naturales, y su piel parece cambiar ligeramente con la luz del entorno. Habla con los animales como si compartieran un mismo lenguaje primitivo, y la naturaleza responde a su presencia con una mezcla de respeto y cautela.','/img/6_5.png'),

(6,6,'Pernoctador de Umbraxis','Tiefling Explorador','Originario de las rutas peligrosas que rodean Umbraxis, este tiefling aprendió a sobrevivir entre persecución, desiertos y fronteras hostiles, convirtiéndolo en un rastreador astuto y adaptable. Viste capas oscuras reforzadas contra el clima y porta armas ligeras ocultas bajo su ropa. Sus ojos brillan con una mezcla de desconfianza y cálculo constante, siempre evaluando rutas de escape antes que amenazas.','/img/6_6.png');

-- =====================================================
-- INSERT HABILIDADES
-- =====================================================

INSERT INTO habilidades (nombre, descripcion, estadistica_base, requisito_minimo) VALUES
('Acrobacias', 'Capacidad para mantener el equilibrio, realizar saltos y movimientos ágiles.', 'Destreza', 16),
('Atletismo', 'Fuerza física usada para trepar, nadar o empujar objetos pesados.', 'Fuerza', 16),
('Conocimiento Arcano', 'Conocimiento sobre magia, hechizos y criaturas mágicas.', 'Inteligencia', 18),
('Engaño', 'Capacidad para mentir, manipular y ocultar intenciones.', 'Carisma', 18),
('Historia', 'Conocimiento sobre reinos, guerras y civilizaciones antiguas.', 'Inteligencia', 16),
('Interpretación', 'Habilidad para actuar, cantar o entretener a otros.', 'Carisma', 16),
('Intimidación', 'Capacidad para infundir miedo mediante presencia o amenazas.', 'Carisma', 16),
('Investigación', 'Buscar pistas, analizar objetos y resolver misterios.', 'Inteligencia', 18),
('Juego de Manos', 'Realizar trucos rápidos con las manos y pequeños robos.', 'Destreza', 18),
('Medicina', 'Conocimiento para tratar heridas y estabilizar aliados.', 'Sabiduría', 16),
('Naturaleza', 'Conocimiento sobre animales, plantas y ecosistemas.', 'Inteligencia', 16),
('Percepción', 'Detectar detalles ocultos mediante los sentidos.', 'Sabiduría', 18),
('Perspicacia', 'Descubrir mentiras y leer emociones o intenciones.', 'Sabiduría', 16),
('Persuasión', 'Convencer y negociar mediante el carisma.', 'Carisma', 18),
('Religión', 'Conocimiento sobre dioses, cultos y rituales.', 'Inteligencia', 16),
('Sigilo', 'Moverse sin ser detectado.', 'Destreza', 18),
('Supervivencia', 'Sobrevivir en entornos salvajes y rastrear criaturas.', 'Sabiduría', 16),
('Trato con Animales', 'Calmar, entrenar y comprender animales.', 'Sabiduría', 16);

-- =====================================================
-- INSERT INVENTARIO
-- =====================================================
-- =====================================================
-- ARMAS CUERPO A CUERPO
-- =====================================================

INSERT INTO inventario (nombre, tipo, subtipo, precio, peso, dano, propiedades, descripcion) VALUES

-- Armas Simples Cuerpo a Cuerpo
('Daga', 'ARMA', 'Simple', '2 gp', '1 lb', '1d4 perforante', 'Ligera, arrojadiza (20/60)', 'Hoja corta y afilada, ideal para combate cercano o lanzar.'),
('Lanza', 'ARMA', 'Simple', '1 gp', '3 lb', '1d6 perforante', 'Arrojadiza (20/60), Versátil (1d8)', 'Palo largo con punta de metal.'),
('Maza', 'ARMA', 'Simple', '5 gp', '4 lb', '1d6 contundente', '-', 'Cabeza de metal pesado en un mango.'),
('Bastón', 'ARMA', 'Simple', '2 sp', '4 lb', '1d6 contundente', 'Versátil (1d8)', 'Bastón de madera resistente, común entre magos.'),

-- Armas Marciales Cuerpo a Cuerpo
('Espada Larga', 'ARMA', 'Marcial', '15 gp', '3 lb', '1d8 cortante', 'Versátil (1d10)', 'Hoja recta de doble filo, equilibrio perfecto.'),
('Hacha de Guerra', 'ARMA', 'Marcial', '10 gp', '4 lb', '1d8 cortante', 'Versátil (1d10)', 'Hacha diseñada específicamente para combate.'),
('Martillo de Guerra', 'ARMA', 'Marcial', '15 gp', '5 lb', '1d8 contundente', 'Versátil (1d10)', 'Martillo pesado con cabeza metálica.'),
('Espada Corta', 'ARMA', 'Marcial', '10 gp', '2 lb', '1d6 perforante', 'Ligera, Sutil', 'Hoja corta ideal para estocadas.'),
('Gran Hacha', 'ARMA', 'Marcial', '1 gp', '7 lb', '1d12 cortante', 'Dos manos', 'Gran hacha de leñador, devastadora en combate.'),

-- Armas Simples a Distancia
('Arco Corto', 'ARMA', 'Simple', '25 gp', '2 lb', '1d6 perforante', 'Munición (80/320), Dos manos', 'Arco pequeño y ligero.'),
('Ballesta Ligera', 'ARMA', 'Simple', '25 gp', '5 lb', '1d8 perforante', 'Munición (80/320), Recarga, Dos manos', 'Ballesta de un solo disparo, fácil de usar.'),

-- Armas Marciales a Distancia

('Arco Largo', 'ARMA', 'Marcial', '50 gp', '2 lb', '1d8 perforante', 'Munición (150/600), Dos manos, Pesada', 'Arco largo de guerra, requiere fuerza.'),
('Arco Largo Élfico', 'ARMA', 'A distancia', '150 gp', '2 lb', '1d8 perforante', 'Alcance 150/600, Dos manos', 'Arco élfico extremadamente preciso y ligero.'),
('Ballesta de Repetición', 'ARMA', 'A distancia', '250 gp', '8 lb', '1d10 perforante', 'Recarga rápida, Alcance 100/400', 'Ballesta mecánica capaz de disparar varios virotes rápidamente.'),

-- bastones
('Bastón Arcano', 'ARMA', 'Bastón', '75 gp', '4 lb', '1d6 contundente', 'Canalizador mágico', 'Bastón usado por magos para amplificar hechizos.'),
('Báculo del Vacío', 'ARMA', 'Bastón', '250 gp', '5 lb', '1d8 contundente', '+1 daño mágico', 'Oscuro báculo alimentado por magia prohibida.'),
('Grimorio Prohibido', 'ARMA', 'Libro Mágico', '300 gp', '2 lb', '1d4 psíquico', 'Permite lanzar hechizos oscuros', 'Libro arcano repleto de hechizos antiguos.'),
('Varita de Cristal', 'ARMA', 'Varita', '150 gp', '1 lb', '1d6 fuerza', '+1 tiradas de hechizo', 'Varita mágica fabricada con cristal encantado.'),
('Cetro Solar', 'ARMA', 'Cetro', '400 gp', '3 lb', '1d8 radiante', 'Daño radiante adicional', 'Cetro bendecido por sacerdotes solares.');

-- =====================================================
-- ARMADURAS
-- =====================================================

INSERT INTO inventario (nombre, tipo, subtipo, precio, peso, propiedades, descripcion) VALUES
-- Tunicas
('Túnica del Aprendiz Arcano', 'ARMADURA', 'Ligera', '35 gp', '3 lb', 'CA 10 + Inteligencia', 'Túnica encantada usada por aprendices de magia.'),
('Manto del Hechicero', 'ARMADURA', 'Ligera', '120 gp', '4 lb', 'CA 11 + Carisma', 'Manto bordado con runas arcanas.'),
('Vestiduras del Archimago', 'ARMADURA', 'Media', '500 gp', '6 lb', 'Resistencia mágica menor', 'Vestiduras antiguas impregnadas de energía arcana.'),
('Túnica de Seda Élfica', 'ARMADURA', 'Ligera', '80 gp', '2 lb', 'Ventaja en Sigilo', 'Seda élfica extremadamente ligera y resistente.'),

-- Armaduras Ligeras
('Armadura de Cuero', 'ARMADURA', 'Ligera', '10 gp', '10 lb', 'CA 11 + Destreza', 'Armadura básica de cuero curtido.'),
('Armadura de Cuero Tachonado', 'ARMADURA', 'Ligera', '45 gp', '13 lb', 'CA 12 + Destreza', 'Cuero reforzado con remaches metálicos.'),
('Armadura de Pieles', 'ARMADURA', 'Ligera', '10 gp', '15 lb', 'CA 11 + Destreza', 'Pieles de animales cosidas.'),

-- Armaduras Medias
('Armadura de Pieles Acorazadas', 'ARMADURA', 'Media', '50 gp', '20 lb', 'CA 14 + Destreza (máx 2)', 'Pieles con placas metálicas.'),
('Cota de Malla', 'ARMADURA', 'Media', '75 gp', '40 lb', 'CA 14 + Destreza (máx 2)', 'Anillos de metal entrelazados.'),
('Cota de Escamas', 'ARMADURA', 'Media', '50 gp', '45 lb', 'CA 14 + Destreza (máx 2)', 'Escamas de metal superpuestas.'),

-- Armaduras Pesadas
('Cota de Anillas', 'ARMADURA', 'Pesada', '30 gp', '40 lb', 'CA 14', 'Anillas de metal cosidas a cuero.'),
('Cota de Malla (Pesada)', 'ARMADURA', 'Pesada', '75 gp', '55 lb', 'CA 16, Desventaja en Sigilo', 'Malla que cubre todo el cuerpo.'),
('Armadura de Placas', 'ARMADURA', 'Pesada', '1500 gp', '65 lb', 'CA 18, Desventaja en Sigilo', 'Placas de metal articuladas que cubren todo el cuerpo.'),

-- Escudos
('Escudo', 'ARMADURA', 'Escudo', '10 gp', '6 lb', '+2 CA, Requiere una mano', 'Escudo de madera reforzado con metal.');

-- =====================================================
-- POCIONES
-- =====================================================

INSERT INTO inventario (nombre, tipo, precio, peso, descripcion) VALUES

('Poción de Curación', 'POCION', '50 gp', '1/2 lb', 'Recuperas 2d4 + 2 puntos de golpe.'),
('Poción de Curación Mayor', 'POCION', '100 gp', '1/2 lb', 'Recuperas 4d4 + 4 puntos de golpe.'),
('Poción de Curación Superior', 'POCION', '500 gp', '1/2 lb', 'Recuperas 8d4 + 8 puntos de golpe.');

-- =====================================================
-- OBJETOS MÁGICOS COMUNES
-- =====================================================

INSERT INTO inventario (nombre, tipo, precio, peso, descripcion) VALUES

('Anillo de Protección', 'OBJETO_MAGICO', '500 gp', '-', '+1 a la Clase de Armadura y salvaciones.'),
('Capa de Elvenca', 'OBJETO_MAGICO', '500 gp', '1 lb', 'Ventaja en pruebas de Sigilo para esconderte.'),
('Botas de Velocidad', 'OBJETO_MAGICO', '500 gp', '1 lb', 'Velocidad base aumenta en 10 pies.'),
('Pergamino de Hechizo', 'OBJETO_MAGICO', '100 gp', '-', 'Contiene un hechizo de nivel 1. Se destruye al lanzarlo.');

-- =====================================================
-- HERRAMIENTAS Y EQUIPO DE AVENTURERO
-- =====================================================

INSERT INTO inventario (nombre, tipo, precio, peso, descripcion) VALUES

('Mochila de Aventurero', 'HERRAMIENTA', '2 gp', '5 lb', 'Capacidad para 30 lb de equipo.'),
('Cuerda (50 pies)', 'HERRAMIENTA', '1 gp', '10 lb', 'Cuerda resistente y duradera.'),
('Antorcha', 'HERRAMIENTA', '1 cp', '1 lb', 'Ilumina 20 pies por 1 hora.'),
('Odre', 'HERRAMIENTA', '2 sp', '5 lb (lleno)', 'Contiene hasta 4 litros de líquido.'),
('Raciones (1 día)', 'HERRAMIENTA', '5 sp', '2 lb', 'Comida seca para un día.'),
('Botiquín de Curas', 'HERRAMIENTA', '5 gp', '3 lb', 'Permite estabilizar a un moribundo.'),
('Kit de ganzuas', 'HERRAMIENTA', '25 gp', '1 lb', 'Permite abrir cerraduras con prueba de Destreza.'),
('Piedra de Afilar', 'HERRAMIENTA', '1 cp', '1 lb', 'Mantiene el filo de las armas.');

-- =====================================================
-- OBJETOS MISCELÁNEOS
-- =====================================================

INSERT INTO inventario (nombre, tipo, precio, peso, descripcion) VALUES

('Yesca y Pedernal', 'MISCELANEO', '5 sp', '-', 'Para encender fuego.'),
('Mapa', 'MISCELANEO', '25 gp', '-', 'Mapa de una región desconocida.'),
('Brújula', 'MISCELANEO', '10 gp', '-', 'Señala el norte magnético.');
