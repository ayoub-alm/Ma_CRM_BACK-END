
INSERT INTO `banks`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES
    ('id-1', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'Banque Populaire (BP)'),
    ('id-2', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'BMCE Bank of Africa'),
    ('id-3', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'Crédit Agricole du Maroc'),
    ('id-4', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'Crédit du Maroc'),
    ('id-5', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'BMCI (Banque Marocaine pour le Commerce et l''Industrie)'),
    ('id-6', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'Société Générale Maroc'),
    ('id-7', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'CIH Bank (Crédit Immobilier et Hôtelier)'),
    ('id-8', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'Al Barid Bank'),
    ('id-9', '2024-09-26 12:00:00', CURRENT_TIME, NULL, CURRENT_TIME, 1, 'Bank Al-Maghrib (Central Bank)');


INSERT INTO `devises`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `code`, `nom_de_la_centime`, `nom_de_la_devise`, `pays`, `symbole`)
VALUES
    ('id-1', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'USD', 'Cent', 'Dollar', 'États-Unis', '$'),
    ('id-2', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'EUR', 'Cent', 'Euro', 'Zone euro', '€'),
    ('id-3', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'JPY', 'Pas', 'Yen', 'Japon', '¥'),
    ('id-4', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'GBP', 'Penny', 'Livre', 'Royaume-Uni', '£'),
    ('id-5', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'AUD', 'Cent', 'Dollar', 'Australie', 'A$'),
    ('id-6', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'CAD', 'Cent', 'Dollar', 'Canada', 'C$'),
    ('id-7', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'CHF', 'Centime', 'Franc', 'Suisse', 'CHF'),
    ('id-8', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'CNY', 'Fen', 'Yuan', 'Chine', '¥'),
    ('id-9', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'INR', 'Paisa', 'Roupie', 'Inde', '₹'),
    ('id-10', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'RUB', 'Kopek', 'Rouble', 'Russie', '₽'),
    ('id-11', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'BRL', 'Centavo', 'Réal', 'Brésil', 'R$'),
    ('id-12', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'ZAR', 'Cent', 'Rand', 'Afrique du Sud', 'R'),
    ('id-13', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'MXN', 'Centavo', 'Peso', 'Mexique', '$'),
    ('id-14', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'KRW', 'Jeon', 'Won', 'Corée du Sud', '₩'),
    ('id-15', CURRENT_TIME, NULL, CURRENT_TIME, NULL, 'MAD', 'Centime', 'Dirham', 'Maroc', 'DH');





INSERT INTO `courts`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES ('id-1',CURRENT_TIME,'',NULL,CURRENT_TIME,1,'Casablanca');

INSERT INTO `company_sizes`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`) VALUES
('id-1', CURRENT_TIME, NULL, NULL, NULL, 1, 'Micro-entreprise : Moins de 10 employés'),
('id-2', CURRENT_TIME, NULL, NULL, NULL, 1, 'Moyenne entreprise : 50 à 249 employés'),
('id-3', CURRENT_TIME, NULL, NULL, NULL, 1, 'Petite entreprise : 10 à 49 employés'),
('id-4', CURRENT_TIME, NULL, NULL, NULL, 1, 'Grande entreprise : Plus de 250 employés');


INSERT INTO `online_payment_methods`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES
    ('id-1', CURRENT_TIME, NULL, NULL, NULL, 1, '2Checkout (Verifone)'),
    ('id-2', CURRENT_TIME, NULL, NULL, NULL, 1, 'Adyen'),
    ('id-3', CURRENT_TIME, NULL, NULL, NULL, 1, 'Al Barid Bank (Digital services)'),
    ('id-4', CURRENT_TIME, NULL, NULL, NULL, 1, 'Alipay'),
    ('id-5', CURRENT_TIME, NULL, NULL, NULL, 1, 'Amazon Pay'),
    ('id-6', CURRENT_TIME, NULL, NULL, NULL, 1, 'Apple Pay'),
    ('id-7', CURRENT_TIME, NULL, NULL, NULL, 1, 'Attijariwafa Bank (Digital services)'),
    ('id-8', CURRENT_TIME, NULL, NULL, NULL, 1, 'Authorize.Net'),
    ('id-9', CURRENT_TIME, NULL, NULL, NULL, 1, 'Banque Populaire (Chaabi Net)'),
    ('id-10', CURRENT_TIME, NULL, NULL, NULL, 1, 'BMCE Bank (Digital services)'),
    ('id-11', CURRENT_TIME, NULL, NULL, NULL, 1, 'BMCI (Digital services)'),
    ('id-12', CURRENT_TIME, NULL, NULL, NULL, 1, 'Bpay'),
    ('id-13', CURRENT_TIME, NULL, NULL, NULL, 1, 'CIH Bank (Digital services)'),
    ('id-14', CURRENT_TIME, NULL, NULL, NULL, 1, 'CMI (Centre Monétique Interbancaire)'),
    ('id-15', CURRENT_TIME, NULL, NULL, NULL, 1, 'Google Pay'),
    ('id-16', CURRENT_TIME, NULL, NULL, NULL, 1, 'Inwi Money'),
    ('id-17', CURRENT_TIME, NULL, NULL, NULL, 1, 'M-Pesa'),
    ('id-18', CURRENT_TIME, NULL, NULL, NULL, 1, 'Maroc Télécommerce'),
    ('id-19', CURRENT_TIME, NULL, NULL, NULL, 1, 'MoneyGram'),
    ('id-20', CURRENT_TIME, NULL, NULL, NULL, 1, 'Orange Money'),
    ('id-21', CURRENT_TIME, NULL, NULL, NULL, 1, 'Payoneer'),
    ('id-22', CURRENT_TIME, NULL, NULL, NULL, 1, 'PayPal'),
    ('id-23', CURRENT_TIME, NULL, NULL, NULL, 1, 'Skrill'),
    ('id-24', CURRENT_TIME, NULL, NULL, NULL, 1, 'Stripe'),
    ('id-25', CURRENT_TIME, NULL, NULL, NULL, 1, 'Wafacash'),
    ('id-26', CURRENT_TIME, NULL, NULL, NULL, 1, 'WeChat Pay'),
    ('id-27', CURRENT_TIME, NULL, NULL, NULL, 1, 'Western Union'),
    ('id-28', CURRENT_TIME, NULL, NULL, NULL, 1, 'Worldpay');


INSERT INTO `job_titles`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES
    ('id-1', CURRENT_TIME, NULL, NULL, NULL, 1, 'Acheteur Senior'),
    ('id-2', CURRENT_TIME, NULL, NULL, NULL, 1, 'Administrateur Systèmes et Réseaux'),
    ('id-3', CURRENT_TIME, NULL, NULL, NULL, 1, 'Agent de Sécurité'),
    ('id-4', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste de Marché International'),
    ('id-5', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste de Projet'),
    ('id-6', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste des Achats'),
    ('id-7', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste des Risques'),
    ('id-8', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste en Innovation'),
    ('id-9', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste Financier'),
    ('id-10', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste IT'),
    ('id-11', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste Marketing'),
    ('id-12', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste Sécurité'),
    ('id-13', CURRENT_TIME, NULL, NULL, NULL, 1, 'Analyste Stratégique'),
    ('id-14', CURRENT_TIME, NULL, NULL, NULL, 1, 'Assistant Administratif'),
    ('id-15', CURRENT_TIME, NULL, NULL, NULL, 1, 'Auditeur Qualité'),
    ('id-16', CURRENT_TIME, NULL, NULL, NULL, 1, 'Avocat d''Entreprise'),
    ('id-17', CURRENT_TIME, NULL, NULL, NULL, 1, 'Business Development Manager'),
    ('id-18', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Communication'),
    ('id-19', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Compte'),
    ('id-20', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Développement Commercial'),
    ('id-21', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Développement des Compétences'),
    ('id-22', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Développement Innovant'),
    ('id-23', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Formation'),
    ('id-24', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Mission Environnementale'),
    ('id-25', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Presse'),
    ('id-26', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chargé de Relation Client'),
    ('id-27', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chef de Projet R&D'),
    ('id-28', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chef de Projet Senior'),
    ('id-29', CURRENT_TIME, NULL, NULL, NULL, 1, 'Communication et Relations'),
    ('id-30', CURRENT_TIME, NULL, NULL, NULL, 1, 'Comptable'),
    ('id-31', CURRENT_TIME, NULL, NULL, NULL, 1, 'Conseiller Client'),
    ('id-32', CURRENT_TIME, NULL, NULL, NULL, 1, 'Consultant en Formation'),
    ('id-33', CURRENT_TIME, NULL, NULL, NULL, 1, 'Consultant en Gestion des Risques'),
    ('id-34', CURRENT_TIME, NULL, NULL, NULL, 1, 'Consultant en Stratégie'),
    ('id-35', CURRENT_TIME, NULL, NULL, NULL, 1, 'Consultant RSE'),
    ('id-36', CURRENT_TIME, NULL, NULL, NULL, 1, 'Contrôleur de Gestion'),
    ('id-37', CURRENT_TIME, NULL, NULL, NULL, 1, 'Contrôleur Qualité'),
    ('id-38', CURRENT_TIME, NULL, NULL, NULL, 1, 'Coordinateur de Projet'),
    ('id-39', CURRENT_TIME, NULL, NULL, NULL, 1, 'Coordinateur International'),
    ('id-40', CURRENT_TIME, NULL, NULL, NULL, 1, 'Coordinateur Logistique'),
    ('id-41', CURRENT_TIME, NULL, NULL, NULL, 1, 'Coordinateur RSE'),
    ('id-42', CURRENT_TIME, NULL, NULL, NULL, 1, 'Designer Graphique'),
    ('id-43', CURRENT_TIME, NULL, NULL, NULL, 1, 'Développeur Informatique'),
    ('id-44', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur Commercial'),
    ('id-45', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur Créatif'),
    ('id-46', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur de l''Innovation'),
    ('id-47', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur de la Stratégie'),
    ('id-48', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur de Projet'),
    ('id-49', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur des Affaires Internationales'),
    ('id-50', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur des Opérations'),
    ('id-51', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur des Relations Publiques'),
    ('id-52', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur des Ressources Humaines'),
    ('id-53', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur des Systèmes d''Information (DSI)'),
    ('id-54', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur Financier (CFO)'),
    ('id-55', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur Général (DG)'),
    ('id-56', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur Juridique'),
    ('id-57', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur Marketing'),
    ('id-58', CURRENT_TIME, NULL, NULL, NULL, 1, 'Directeur R&D'),
    ('id-59', CURRENT_TIME, NULL, NULL, NULL, 1, 'Fonctions Stratégiques et Innovantes'),
    ('id-60', CURRENT_TIME, NULL, NULL, NULL, 1, 'Formateur'),
    ('id-61', CURRENT_TIME, NULL, NULL, NULL, 1, 'Gérant'),
    ('id-62', CURRENT_TIME, NULL, NULL, NULL, 1, 'Gestionnaire de Chaîne d''Approvisionnement'),
    ('id-63', CURRENT_TIME, NULL, NULL, NULL, 1, 'Gestionnaire des Approvisionnements'),
    ('id-64', CURRENT_TIME, NULL, NULL, NULL, 1, 'Gestionnaire des Relations Médias'),
    ('id-65', CURRENT_TIME, NULL, NULL, NULL, 1, 'Gestionnaire des Relations Sociales'),
    ('id-66', CURRENT_TIME, NULL, NULL, NULL, 1, 'Illustrateur'),
    ('id-67', CURRENT_TIME, NULL, NULL, NULL, 1, 'Ingénieur Assurance Qualité'),
    ('id-68', CURRENT_TIME, NULL, NULL, NULL, 1, 'Ingénieur Recherche et Développement'),
    ('id-69', CURRENT_TIME, NULL, NULL, NULL, 1, 'Juriste d''Entreprise'),
    ('id-70', CURRENT_TIME, NULL, NULL, NULL, 1, 'Office Manager'),
    ('id-71', CURRENT_TIME, NULL, NULL, NULL, 1, 'Planificateur de Production'),
    ('id-72', CURRENT_TIME, NULL, NULL, NULL, 1, 'Planificateur Logistique'),
    ('id-73', CURRENT_TIME, NULL, NULL, NULL, 1, 'Président-Directeur Général (PDG)'),
    ('id-74', CURRENT_TIME, NULL, NULL, NULL, 1, 'Représentant Commercial'),
    ('id-75', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Administratif'),
    ('id-76', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Commercial'),
    ('id-77', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Communication Externe'),
    ('id-78', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Conformité'),
    ('id-79', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Conformité et Risques'),
    ('id-80', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable des Achats'),
    ('id-81', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable des Opérations'),
    ('id-82', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable des Ventes'),
    ('id-83', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Design Produit'),
    ('id-84', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Développement des Affaires'),
    ('id-85', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Développement Durable'),
    ('id-86', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Expansion Internationale'),
    ('id-87', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Formation'),
    ('id-88', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Gestion des Risques'),
    ('id-89', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Innovation'),
    ('id-90', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Logistique'),
    ('id-91', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Marketing Digital'),
    ('id-92', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Partenariats'),
    ('id-93', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Planification Stratégique'),
    ('id-94', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Qualité'),
    ('id-95', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Recrutement'),
    ('id-96', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Sécurité'),
    ('id-97', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Sécurité Informatique'),
    ('id-98', CURRENT_TIME, NULL, NULL, NULL, 1, 'Responsable Service Client'),
    ('id-99', CURRENT_TIME, NULL, NULL, NULL, 1, 'Scientifique Senior'),
    ('id-100', CURRENT_TIME, NULL, NULL, NULL, 1, 'Secrétaire Exécutif'),
    ('id-101', CURRENT_TIME, NULL, NULL, NULL, 1, 'Support Client Technique'),
    ('id-102', CURRENT_TIME, NULL, NULL, NULL, 1, 'Support et Gestion des Risques');



INSERT INTO `industries`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES
    ('id-1', CURRENT_TIME, NULL, NULL, NULL, 1, 'Aéronautique et spatial'),
    ('id-2', CURRENT_TIME, NULL, NULL, NULL, 1, 'Agriculture'),
    ('id-3', CURRENT_TIME, NULL, NULL, NULL, 1, 'Agritech'),
    ('id-4', CURRENT_TIME, NULL, NULL, NULL, 1, 'Alimentation et boissons'),
    ('id-5', CURRENT_TIME, NULL, NULL, NULL, 1, 'Arts et culture'),
    ('id-6', CURRENT_TIME, NULL, NULL, NULL, 1, 'Biotechnologie'),
    ('id-7', CURRENT_TIME, NULL, NULL, NULL, 1, 'Chimie'),
    ('id-8', CURRENT_TIME, NULL, NULL, NULL, 1, 'Commerce de détail'),
    ('id-9', CURRENT_TIME, NULL, NULL, NULL, 1, 'Construction'),
    ('id-10', CURRENT_TIME, NULL, NULL, NULL, 1, 'Développement durable et environnement'),
    ('id-11', CURRENT_TIME, NULL, NULL, NULL, 1, 'E-santé (Health Tech)'),
    ('id-12', CURRENT_TIME, NULL, NULL, NULL, 1, 'Économie circulaire'),
    ('id-13', CURRENT_TIME, NULL, NULL, NULL, 1, 'Éducation'),
    ('id-14', CURRENT_TIME, NULL, NULL, NULL, 1, 'Énergie et services publics'),
    ('id-15', CURRENT_TIME, NULL, NULL, NULL, 1, 'Finance'),
    ('id-16', CURRENT_TIME, NULL, NULL, NULL, 1, 'Hôtellerie et tourisme'),
    ('id-17', CURRENT_TIME, NULL, NULL, NULL, 1, 'Immobilier'),
    ('id-18', CURRENT_TIME, NULL, NULL, NULL, 1, 'Industrie manufacturière'),
    ('id-19', CURRENT_TIME, NULL, NULL, NULL, 1, 'Jeux vidéo'),
    ('id-20', CURRENT_TIME, NULL, NULL, NULL, 1, 'Logistique et chaîne d''approvisionnement'),
    ('id-21', CURRENT_TIME, NULL, NULL, NULL, 1, 'Médias et divertissements'),
    ('id-22', CURRENT_TIME, NULL, NULL, NULL, 1, 'Mode et textile'),
    ('id-23', CURRENT_TIME, NULL, NULL, NULL, 1, 'Ressources naturelles'),
    ('id-24', CURRENT_TIME, NULL, NULL, NULL, 1, 'Santé'),
    ('id-25', CURRENT_TIME, NULL, NULL, NULL, 1, 'Services aux entreprises'),
    ('id-26', CURRENT_TIME, NULL, NULL, NULL, 1, 'Services financiers alternatifs'),
    ('id-27', CURRENT_TIME, NULL, NULL, NULL, 1, 'Services professionnels'),
    ('id-28', CURRENT_TIME, NULL, NULL, NULL, 1, 'Technologie'),
    ('id-29', CURRENT_TIME, NULL, NULL, NULL, 1, 'Télécommunications'),
    ('id-30', CURRENT_TIME, NULL, NULL, NULL, 1, 'Transports et logistique'),
    ('id-31', CURRENT_TIME, NULL, NULL, NULL, 1, 'Vente en gros et distribution');



INSERT INTO `delivery_methods`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES ('id-1', CURRENT_TIME, NULL, NULL, NULL, 1, 'Expédition standard'),
       ('id-2', CURRENT_TIME, NULL, NULL, NULL, 1, 'Livraison à domicile'),
       ('id-3', CURRENT_TIME, NULL, NULL, NULL, 1, 'Livraison avec suivi'),
       ('id-4', CURRENT_TIME, NULL, NULL, NULL, 1, 'Livraison en point relais'),
       ('id-5', CURRENT_TIME, NULL, NULL, NULL, 1, 'Livraison express'),
       ('id-6', CURRENT_TIME, NULL, NULL, NULL, 1, 'Livraison internationale'),
       ('id-7', CURRENT_TIME, NULL, NULL, NULL, 1, 'Livraison par coursier'),
       ('id-8', CURRENT_TIME, NULL, NULL, NULL, 1, 'Livraison programmée'),
       ('id-9', CURRENT_TIME, NULL, NULL, NULL, 1, 'Non applicable'),
       ('id-10', CURRENT_TIME, NULL, NULL, NULL, 1, 'Ramassage en magasin');



INSERT INTO `legal_status` (`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`) VALUES
(1, NOW(), 'system', NULL, NOW(), 1, 'Association'),
(2, NOW(), 'system', NULL, NOW(), 1, 'Auto-entrepreneur'),
(3, NOW(), 'system', NULL, NOW(), 1, 'Coopérative'),
(4, NOW(), 'system', NULL, NOW(), 1, 'Entreprise de l économie sociale et solidaire (ESS)'),
(5, NOW(), 'system', NULL, NOW(), 1, 'Entreprise Familiale'),
(6, NOW(), 'system', NULL, NOW(), 1, 'Entreprise Individuelle (EI)'),
(7, NOW(), 'system', NULL, NOW(), 1, 'Entreprise Publique'),
(8, NOW(), 'system', NULL, NOW(), 1, 'Entreprise Sociale'),
(9, NOW(), 'system', NULL, NOW(), 1, 'Entreprise Unipersonnelle à Responsabilité Limitée (EURL)'),
(10, NOW(), 'system', NULL, NOW(), 1, 'Groupement dIntérêt Économique (GIE)'),
(11, NOW(), 'system', NULL, NOW(), 1, 'Non Applicable'),
(12, NOW(), 'system', NULL, NOW(), 1, 'Personne physique'),
(13, NOW(), 'system', NULL, NOW(), 1, 'Société à Responsabilité Limitée (SARL)'),
(14, NOW(), 'system', NULL, NOW(), 1, 'Société à Responsabilité Limitée à Associé Unique (SARLU)'),
(15, NOW(), 'system', NULL, NOW(), 1, 'Société Anonyme (SA)'),
(16, NOW(), 'system', NULL, NOW(), 1, 'Société Civile (SC)'),
(17, NOW(), 'system', NULL, NOW(), 1, 'Société Civile de Moyens (SCM)'),
(18, NOW(), 'system', NULL, NOW(), 1, 'Société Civile Immobilière (SCI)'),
(19, NOW(), 'system', NULL, NOW(), 1, 'Société Coopérative de Production (SCOP)'),
(20, NOW(), 'system', NULL, NOW(), 1, 'Société Exercice Libéral (SEL)'),
(21, NOW(), 'system', NULL, NOW(), 1, 'Société en Commandite par Actions (SCA)'),
(22, NOW(), 'system', NULL, NOW(), 1, 'Société en Commandite Simple (SCS)'),
(23, NOW(), 'system', NULL, NOW(), 1, 'Société en Nom Collectif (SNC)'),
(24, NOW(), 'system', NULL, NOW(), 1, 'Société en Participation (SEP)'),
(25, NOW(), 'system', NULL, NOW(), 1, 'Société par Actions Simplifiée (SAS)'),
(26, NOW(), 'system', NULL, NOW(), 1, 'Société par Actions Simplifiée Unipersonnelle (SASU)');



INSERT INTO `countries`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES ('',CURRENT_DATE,'','',CURRENT_DATE,1,'Tanger');


INSERT INTO `countries`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`)
VALUES ('',CURRENT_DATE,'','',CURRENT_DATE,1,'casablanca');



INSERT INTO `supplier_types` (`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`) VALUES
(1, NOW(), 'system', NULL, NOW(), 1, 'Prestataire'),
(2, NOW(), 'system', NULL, NOW(), 1, 'Grossiste'),
(3, NOW(), 'system', NULL, NOW(), 1, 'Distributeur'),
(4, NOW(), 'system', NULL, NOW(), 1, 'Fabricant'),
(5, NOW(), 'system', NULL, NOW(), 1, 'Importateur'),
(6, NOW(), 'system', NULL, NOW(), 1, 'Exportateur'),
(7, NOW(), 'system', NULL, NOW(), 1, 'Agent'),
(8, NOW(), 'system', NULL, NOW(), 1, 'Broker'),
(9, NOW(), 'system', NULL, NOW(), 1, 'Fournisseur de services'),
(10, NOW(), 'system', NULL, NOW(), 1, 'Sous-traitant'),
(11, NOW(), 'system', NULL, NOW(), 1, 'Partenaire stratégique'),
(12, NOW(), 'system', NULL, NOW(), 1, 'Fournisseur de matières premières'),
(13, NOW(), 'system', NULL, NOW(), 1, 'Fournisseur de solutions'),
(14, NOW(), 'system', NULL, NOW(), 1, 'Fournisseur de technologie'),
(15, NOW(), 'system', NULL, NOW(), 1, 'Fournisseur de logistique'),
(16, NOW(), 'system', NULL, NOW(), 1, 'Transporteur'),
(17, NOW(), 'system', NULL, NOW(), 1, 'Revendeur');


INSERT INTO `unit_of_measurements`  (`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`) VALUES
(1, NOW(), 'system', NULL, NOW(), 1, 'Année'),
(2, NOW(), 'system', NULL, NOW(), 1, 'Are (a)'),
(3, NOW(), 'system', NULL, NOW(), 1, 'Atmosphère (atm)'),
(4, NOW(), 'system', NULL, NOW(), 1, 'Bar'),
(5, NOW(), 'system', NULL, NOW(), 1, 'Boîte'),
(6, NOW(), 'system', NULL, NOW(), 1, 'Calorie (cal)'),
(7, NOW(), 'system', NULL, NOW(), 1, 'Centilitre (cL)'),
(8, NOW(), 'system', NULL, NOW(), 1, 'Centimètre (cm)'),
(9, NOW(), 'system', NULL, NOW(), 1, 'Centimètre carré (cm²)'),
(10, NOW(), 'system', NULL, NOW(), 1, 'Cubic foot (ft³)'),
(11, NOW(), 'system', NULL, NOW(), 1, 'Degré Celsius (°C)'),
(12, NOW(), 'system', NULL, NOW(), 1, 'Degré Fahrenheit (°F)'),
(13, NOW(), 'system', NULL, NOW(), 1, 'Gallon (gal)'),
(14, NOW(), 'system', NULL, NOW(), 1, 'Gramme (g)'),
(15, NOW(), 'system', NULL, NOW(), 1, 'Hectare (ha)'),
(16, NOW(), 'system', NULL, NOW(), 1, 'Heure (h)'),
(17, NOW(), 'system', NULL, NOW(), 1, 'Joule (J)'),
(18, NOW(), 'system', NULL, NOW(), 1, 'Jour'),
(19, NOW(), 'system', NULL, NOW(), 1, 'Kelvin (K)'),
(20, NOW(), 'system', NULL, NOW(), 1, 'Kilocalorie (kcal)'),
(21, NOW(), 'system', NULL, NOW(), 1, 'Kilogramme (kg)'),
(22, NOW(), 'system', NULL, NOW(), 1, 'Kilojoule (kJ)'),
(23, NOW(), 'system', NULL, NOW(), 1, 'Kilomètre (km)'),
(24, NOW(), 'system', NULL, NOW(), 1, 'Kilomètre carré (km²)'),
(25, NOW(), 'system', NULL, NOW(), 1, 'Kilomètre par heure (km/h)'),
(26, NOW(), 'system', NULL, NOW(), 1, 'Kilowattheure (kWh)'),
(27, NOW(), 'system', NULL, NOW(), 1, 'Litre (L)'),
(28, NOW(), 'system', NULL, NOW(), 1, 'Livre (lb)'),
(29, NOW(), 'system', NULL, NOW(), 1, 'Lot'),
(30, NOW(), 'system', NULL, NOW(), 1, 'Mètre (m)'),
(31, NOW(), 'system', NULL, NOW(), 1, 'Mètre carré (m²)'),
(32, NOW(), 'system', NULL, NOW(), 1, 'Mètre cube (m³)'),
(33, NOW(), 'system', NULL, NOW(), 1, 'Mètre par seconde (m/s)'),
(34, NOW(), 'system', NULL, NOW(), 1, 'Mile (mi)'),
(35, NOW(), 'system', NULL, NOW(), 1, 'Mile par heure (mph)'),
(36, NOW(), 'system', NULL, NOW(), 1, 'Milligramme (mg)'),
(37, NOW(), 'system', NULL, NOW(), 1, 'Millilitre (mL)'),
(38, NOW(), 'system', NULL, NOW(), 1, 'Millimètre (mm)'),
(39, NOW(), 'system', NULL, NOW(), 1, 'Millimètre de mercure (mmHg)'),
(40, NOW(), 'system', NULL, NOW(), 1, 'Minute (min)'),
(41, NOW(), 'system', NULL, NOW(), 1, 'Mois'),
(42, NOW(), 'system', NULL, NOW(), 1, 'Once (oz)'),
(43, NOW(), 'system', NULL, NOW(), 1, 'Pack'),
(44, NOW(), 'system', NULL, NOW(), 1, 'Pascal (Pa)'),
(45, NOW(), 'system', NULL, NOW(), 1, 'Pied (ft)'),
(46, NOW(), 'system', NULL, NOW(), 1, 'Pied carré (ft²)'),
(47, NOW(), 'system', NULL, NOW(), 1, 'Pint (pt)'),
(48, NOW(), 'system', NULL, NOW(), 1, 'Pouce (in)'),
(49, NOW(), 'system', NULL, NOW(), 1, 'Pound per square inch (psi)'),
(50, NOW(), 'system', NULL, NOW(), 1, 'Quart (qt)'),
(51, NOW(), 'system', NULL, NOW(), 1, 'Seconde (s)'),
(52, NOW(), 'system', NULL, NOW(), 1, 'Semaine'),
(53, NOW(), 'system', NULL, NOW(), 1, 'Siège'),
(54, NOW(), 'system', NULL, NOW(), 1, 'Tonne (t)'),
(55, NOW(), 'system', NULL, NOW(), 1, 'Unité (unit)'),
(56, NOW(), 'system', NULL, NOW(), 1, 'Wattheure (Wh)'),
(57, NOW(), 'system', NULL, NOW(), 1, 'Yard (yd)'),
(58, NOW(), 'system', NULL, NOW(), 1, 'Yard carré (yd²)');


INSERT INTO `cities` (`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`) VALUES
          (NULL, NOW(), 'Syetem', '', NOW(), '', 'Casablanca'),
            (NULL, NOW(), 'Syetem', '', NOW(), '', 'Tanger');


INSERT INTO `departments`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`) VALUES
            (1, NOW(), 'system', NULL, NOW(), 1, 'Achats et Approvisionnement'),
            (2, NOW(), 'system', NULL, NOW(), 1, 'Administration'),
            (3, NOW(), 'system', NULL, NOW(), 1, 'Affaires Internationales'),
            (4, NOW(), 'system', NULL, NOW(), 1, 'Design et Création'),
            (5, NOW(), 'system', NULL, NOW(), 1, 'Développement des Affaires'),
            (6, NOW(), 'system', NULL, NOW(), 1, 'Développement Durable et Responsabilité Sociétale (RSE)'),
            (7, NOW(), 'system', NULL, NOW(), 1, 'Finances'),
            (8, NOW(), 'system', NULL, NOW(), 1, 'Formation et Développement'),
            (9, NOW(), 'system', NULL, NOW(), 1, 'Gestion de Projets'),
            (10, NOW(), 'system', NULL, NOW(), 1, 'Gestion des Risques'),
            (11, NOW(), 'system', NULL, NOW(), 1, 'Innovation'),
            (12, NOW(), 'system', NULL, NOW(), 1, 'Juridique et Conformité'),
            (13, NOW(), 'system', NULL, NOW(), 1, 'Logistique'),
            (14, NOW(), 'system', NULL, NOW(), 1, 'Marketing'),
            (15, NOW(), 'system', NULL, NOW(), 1, 'Opérations'),
            (16, NOW(), 'system', NULL, NOW(), 1, 'Qualité et Assurance Qualité'),
            (17, NOW(), 'system', NULL, NOW(), 1, 'Recherche et Développement (R&D)'),
            (18, NOW(), 'system', NULL, NOW(), 1, 'Relations Publiques'),
            (19, NOW(), 'system', NULL, NOW(), 1, 'Ressources Humaines (RH)'),
            (20, NOW(), 'system', NULL, NOW(), 1, 'Sécurité'),
            (21, NOW(), 'system', NULL, NOW(), 1, 'Service Client'),
            (22, NOW(), 'system', NULL, NOW(), 1, 'Stratégie et Planification'),
(23, NOW(), 'system', NULL, NOW(), 1, "Technologie de l\'Information (TI)"),
(24, NOW(), 'system', NULL, NOW(), 1, 'Ventes');


INSERT INTO `payment_methods`(`id`, `created_at`, `created_by`, `deleted_at`, `updated_at`, `active`, `name`) VALUES
  (1, NOW(), 'system', NULL, NOW(), 1, 'Carte de crédit'),
  (2, NOW(), 'system', NULL, NOW(), 1, 'Carte de débit'),
  (3, NOW(), 'system', NULL, NOW(), 1, 'Chèque'),
  (4, NOW(), 'system', NULL, NOW(), 1, 'Espèces'),
  (5, NOW(), 'system', NULL, NOW(), 1, 'Paiement numérique'),
  (6, NOW(), 'system', NULL, NOW(), 1, 'Prélèvement automatique'),
  (7, NOW(), 'system', NULL, NOW(), 1, 'Traite'),
  (8, NOW(), 'system', NULL, NOW(), 1, 'Virement');

INSERT INTO `proprietary_structures` (`id`, `active`, `name`, `created_at`, `created_by`, `deleted_at`, `updated_at`)
VALUES (NULL, b'1', 'Privé', NOW(), NULL, NULL, NOW()),
                    (NULL, b'1', 'Public', NOW(), NULL, NULL, NOW());


-- Inserting a record for 'M'
INSERT INTO titles (created_at, created_by, deleted_at, updated_at, active, title)
VALUES (CURRENT_TIMESTAMP(6), 'user1', NULL, NULL, b'1', 'M');
-- Inserting a record for 'Mem'
INSERT INTO titles (created_at, created_by, deleted_at, updated_at, active, title)
VALUES (CURRENT_TIMESTAMP(6), 'user2', NULL, NULL, b'1', 'Mem');
