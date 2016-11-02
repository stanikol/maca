--- Add some values
# --- !Ups
insert into categories values('Макаруны');
insert into categories values('Наборы');
insert into goods(price, category, title, description, image) values(1.01, 'Макаруны', 'title', 'description',   ARRAY['img/maca1.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(price, category, title, description, image) values(1.01, 'Макаруны', 'title2', 'description2', ARRAY['img/maca2.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(price, category, title, description, image) values(1.01, 'Макаруны', 'title3', 'description3', ARRAY['img/maca3.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(price, category, title, description, image) values(1.01, 'Макаруны', 'title4', 'description4', ARRAY['img/maca1.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(price, category, title, description, image) values(1.01, 'Макаруны', 'title5', 'description5', ARRAY['img/maca3.jpg','img/maca2.jpg', 'img/maca3.jpg']);

# --- !Downs
delete from goods;
delete from categories;