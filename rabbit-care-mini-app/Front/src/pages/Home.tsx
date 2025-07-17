import React, { useState } from 'react';
import AddressMap from '../components/AddressMap.tsx';

const Home: React.FC = () => {
  const [latitude, setLatitude] = useState<number | null>(null);
  const [longitude, setLongitude] = useState<number | null>(null);
  const [street, setStreet] = useState<string>('');
  const [streetNumber, setStreetNumber] = useState<string>('');
  const [city, setCity] = useState<string>('');

  return (
    <div>
      <h1>Welcome to Bunny Care Provider</h1>
      <p>Your one-stop solution for all rabbit care needs.</p>
      
      <AddressMap
        latitude={latitude}
        longitude={longitude}
        setLatitude={setLatitude}
        setLongitude={setLongitude}
        setStreet={setStreet}
        setStreetNumber={setStreetNumber}
        setCity={setCity}
      />

      <div style={{ marginTop: '20px' }}>
        <h2>Selected Location</h2>
        <p><strong>Latitude:</strong> {latitude}</p>
        <p><strong>Longitude:</strong> {longitude}</p>
        <p><strong>Street:</strong> {street}</p>
        <p><strong>Street Number:</strong> {streetNumber}</p>
        <p><strong>City:</strong> {city}</p>
      </div>
    </div>
  );
};

export default Home;
