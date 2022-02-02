const colors = require("tailwindcss/colors");

module.exports = {
  content: ["./index.html"],

  theme: {
    extend: {
      colors: {
        cyan: colors.cyan,
      },
    },
  },
  variants: {
    extend: {
      backgroundColor: ["active"],
    },
  },
  plugins: [],
};
