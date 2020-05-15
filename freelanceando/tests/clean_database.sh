BASEDIR=test_database
mkdir $BASEDIR
find $BASEDIR -name '*.json' -delete

# Minified json
UTILS='{"freelancer":{"valid_freelancers":[{"username":"ada_lovelace","hourly_price":100,"country_code":"EN","reputation":"expert","category_ids":[1,2]},{"username":"alan_turing","hourly_price":100,"country_code":"EN","reputation":"expert","category_ids":[1,2]},{"username":"daniel_penazzi","hourly_price":90,"country_code":"AR","reputation":"senior","category_ids":[1,2]}],"key_id_error":{"id":1,"username":"ada_lovelace","hourly_price":100,"country_code":"EN","reputation":"expert","category_ids":[1,2]}},"client":{"valid_clients":[{"username":"client1","country_code":"AR"},{"username":"client2","country_code":"AR"},{"username":"client3","country_code":"AR"}],"key_id_error":{"id":1,"username":"client1","country_code":"AR"}},"job":{"valid_jobs":[{"title":"job1","category_id":1,"client_id":1,"preferred_expertise":"junior","preferred_country":"AR","hourly_price":40},{"title":"job2","category_id":2,"client_id":2,"preferred_expertise":"junior","preferred_country":"AR","hourly_price":40},{"title":"job3","category_id":1,"client_id":3,"preferred_expertise":"junior","preferred_country":"AR","hourly_price":40}],"key_id_error":{"id":10,"title":"job1","category_id":1,"client_id":1,"preferred_expertise":"junior","preferred_country":"AR","hourly_price":40}}}'

echo '[{"name": "Computer Science Theory", "id": 1},{"name": "Cryptography", "id": 2}]' > ${BASEDIR}/categories.json
echo $UTILS > ${BASEDIR}/utils.json
echo "Database configured correctly at ${BASEDIR}/"
