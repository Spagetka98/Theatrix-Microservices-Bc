module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      screens: {
        "3xl": "2500px",
        "4xl": "3800px",
      },
    },
  },
  plugins: [require("tailwind-scrollbar-hide")],
};
