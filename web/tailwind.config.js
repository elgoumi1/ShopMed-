/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        brand: {
          navy: '#0A192F',
          dark: '#0B1320',
          primary: '#0F3B5F',
          emerald: '#059669',
          amber: '#D97706',
          deal: '#DC2626'
        }
      }
    },
  },
  plugins: [],
}
