/* eslint-env jest */
'use strict';

module.exports = {
  isRooted: jest.fn(() => Promise.resolve(false)),
  containsSignatures: jest.fn(() => Promise.resolve(true)),
};
