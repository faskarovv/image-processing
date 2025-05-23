CREATE TABLE users (
                       user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       enabled BOOLEAN DEFAULT false,
                       verification_code VARCHAR(255),
                       verification_expiration TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_roles (
                            user_id UUID REFERENCES users(user_id) ON DELETE CASCADE,
                            role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);


CREATE TABLE images (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        name VARCHAR(255) NOT NULL,
                        content_type VARCHAR(255) NOT NULL,
                        file_size BIGINT NOT NULL,
                        image_data BYTEA,
                        upload_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        original_image_id UUID REFERENCES images(id),
                        transformation_type VARCHAR(255),
                        owner_id UUID REFERENCES users(user_id) ON DELETE CASCADE
);

insert into roles (name) values
                             ('user'),
                             ('admin');

select * from roles