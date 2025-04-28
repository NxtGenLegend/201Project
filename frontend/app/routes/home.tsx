import '../styles/Home.css';
import React, { Suspense, useState } from 'react';

const Header = React.lazy(() => import('../components/Header/Header'));

export default function Home() {
  const [searchValue, setSearchValue] = useState(''); // add state to control input

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <main style={{ padding: '1rem' }}>
        <div className="search-container">
          <input
            type="text"
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
            placeholder="Search..."
            aria-label="Search input"
            className="search-input"
          />
        </div>
      </main>
    </Suspense>
  );
}