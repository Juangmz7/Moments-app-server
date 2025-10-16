-- ======================================
-- DROP DEPENDENT TABLES FIRST (optional for full reset)
-- ======================================
-- DROP TABLE IF EXISTS event_participant CASCADE;
-- DROP TABLE IF EXISTS event_bio_tags CASCADE;
-- DROP TABLE IF EXISTS user_bio_tags CASCADE;
-- DROP TABLE IF EXISTS user_languages CASCADE;
-- DROP TABLE IF EXISTS event_organiser_events CASCADE;
-- DROP TABLE IF EXISTS event CASCADE;
-- DROP TABLE IF EXISTS event_bio CASCADE;
-- DROP TABLE IF EXISTS event_organiser CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;
-- DROP TABLE IF EXISTS user_profile CASCADE;
-- ======================================


-- ======================================
-- USERS & USER PROFILE
-- ======================================

-- USER PROFILES
INSERT INTO user_profile (id, user_name, age, nationality, city, country)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', 22, 'Spain', 'Leuven', 'Belgium'),
    ('22222222-2222-2222-2222-222222222222', 'Bob', 25, 'Netherlands', 'Leuven', 'Belgium')
    ON CONFLICT (id) DO NOTHING;

-- USER LANGUAGES
INSERT INTO user_languages (user_profile_id, language)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Spanish'),
    ('11111111-1111-1111-1111-111111111111', 'English'),
    ('22222222-2222-2222-2222-222222222222', 'Dutch'),
    ('22222222-2222-2222-2222-222222222222', 'English')
    ON CONFLICT DO NOTHING;

-- USER INTEREST TAGS
INSERT INTO user_bio_tags (user_bio_id, tag)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'MUSIC'),
    ('11111111-1111-1111-1111-111111111111', 'FOOD'),
    ('22222222-2222-2222-2222-222222222222', 'TECHNOLOGY'),
    ('22222222-2222-2222-2222-222222222222', 'SPORTS')
    ON CONFLICT DO NOTHING;

-- USERS
INSERT INTO users (user_id, email, user_profile_id, is_active)
VALUES
    ('aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'alice@mail.com', '11111111-1111-1111-1111-111111111111', true),
    ('bbbb2222-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'bob@mail.com', '22222222-2222-2222-2222-222222222222', true)
    ON CONFLICT (user_id) DO NOTHING;

-- ======================================
-- EVENTS
-- ======================================

-- EVENT BIO
INSERT INTO event_bio (id, description, image)
VALUES
    ('33333333-3333-3333-3333-333333333333', 'Tech innovation meetup', NULL),
    ('44444444-4444-4444-4444-444444444444', 'Music jam session', NULL)
    ON CONFLICT (id) DO NOTHING;

-- EVENT BIO TAGS
INSERT INTO event_bio_tags (event_bio_id, tag)
VALUES
    ('33333333-3333-3333-3333-333333333333', 'TECHNOLOGY'),
    ('33333333-3333-3333-3333-333333333333', 'EDUCATION'),
    ('44444444-4444-4444-4444-444444444444', 'MUSIC'),
    ('44444444-4444-4444-4444-444444444444', 'ART')
    ON CONFLICT DO NOTHING;

-- ORGANISERS
INSERT INTO event_organiser (id, user_profile_id)
VALUES
    ('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111'),
    ('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222')
    ON CONFLICT (id) DO NOTHING;

-- EVENTS
INSERT INTO event (event_id, name, event_bio_id, organiser_id, city, place_name, start_date, end_date, event_status)
VALUES
    ('77777777-7777-7777-7777-777777777777', 'AI Workshop', '33333333-3333-3333-3333-333333333333', '55555555-5555-5555-5555-555555555555',
     'Leuven', 'Campus Hall', '2025-10-20T14:00:00', '2025-10-20T18:00:00', 'ACTIVE'),

    ('88888888-8888-8888-8888-888888888888', 'Live Music Night', '44444444-4444-4444-4444-444444444444', '66666666-6666-6666-6666-666666666666',
     'Leuven', 'Student Bar', '2025-10-22T20:00:00', '2025-10-22T23:30:00', 'ACTIVE')
    ON CONFLICT (event_id) DO NOTHING;

-- EVENT PARTICIPANTS
INSERT INTO event_participant (id, user_profile_id, event_id)
VALUES
    ('99999999-9999-9999-9999-999999999999', '11111111-1111-1111-1111-111111111111', '88888888-8888-8888-8888-888888888888'),
    ('aaaa9999-aaaa-9999-aaaa-aaaaaaaa9999', '22222222-2222-2222-2222-222222222222', '77777777-7777-7777-7777-777777777777')
    ON CONFLICT (id) DO NOTHING;
