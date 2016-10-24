--- Add some values
# --- !Ups
insert into categories values('Макаруны');
insert into categories values('Наборы');
insert into goods(category, title, description, image) values('Макаруны', 'title', 'description',   ARRAY['img/maca1.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title2', 'description2', ARRAY['img/maca2.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title3', 'description3', ARRAY['img/maca3.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title4', 'description4', ARRAY['img/maca1.jpg','img/maca2.jpg', 'img/maca3.jpg']);
insert into goods(category, title, description, image) values('Макаруны', 'title5', 'description5', ARRAY['img/maca3.jpg','img/maca2.jpg', 'img/maca3.jpg']);

# --- !Downs
delete from goods;
delete from categories;