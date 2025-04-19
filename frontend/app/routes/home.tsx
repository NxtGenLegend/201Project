import React, { Suspense } from 'react';
const Header = React.lazy(() => import('../components/Header/Header'));

export default function Home() {
  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <main style={{ padding: '1rem' }}>
      </main>
    </Suspense>
  );
}