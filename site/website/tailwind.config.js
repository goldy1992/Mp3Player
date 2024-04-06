/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    `./src/pages/**/*.{js,jsx,ts,tsx}`,
    `./src/components/**/*.{js,jsx,ts,tsx}`,
    `./gatsby-browser.tsx`
  ],
  // theme: {
  //   //extend: {},
  // },
  plugins: [],
  darkMode: 'class'
  // corePlugins: {
  //   preflight: false
  // }
}
